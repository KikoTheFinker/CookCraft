package it.project.cookcraft.controllers;

import it.project.cookcraft.dto.ReviewRequestDTO;
import it.project.cookcraft.models.Order;
import it.project.cookcraft.models.User;
import it.project.cookcraft.security.JwtUtil;
import it.project.cookcraft.services.EmailService;
import it.project.cookcraft.services.interfaces.OrderService;
import it.project.cookcraft.services.interfaces.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;
    private final UserService userService;
    private final EmailService emailService;
    private final JwtUtil jwtUtil;

    public OrderController(OrderService orderService,
                           UserService userService, EmailService emailService, JwtUtil jwtUtil) {
        this.orderService = orderService;
        this.userService = userService;
        this.emailService = emailService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping
    public ResponseEntity<Order> saveOrder(@RequestHeader("Authorization") String token, @RequestBody Order order) {
        String jwtToken = token.replace("Bearer ", "");
        String userEmail = jwtUtil.extractEmail(jwtToken);
        Optional<User> user = userService.findUserByEmail(userEmail);

        if (user.isPresent()) {
            order.setUserId(user.get().getId());
            List<Order> activeOrders = orderService.findOrdersByUserIdAndIsFinished(user.get().getId(), false);
            if (!activeOrders.isEmpty()) {
                return ResponseEntity.ok(activeOrders.get(0));
            }
            Long id = orderService.save(order);
            order.setId(id);
            return ResponseEntity.ok(order);
        } else {
            return ResponseEntity.status(403).body(null);
        }
    }

    @GetMapping("/status/{id}")
    public ResponseEntity<String> getOrderStatus(@PathVariable Long id) {
        Optional<Order> order = orderService.findOrderById(id);

        if (order.isPresent()) {
            Order currentOrder = order.get();

            if (currentOrder.isFinished()) {
                if (currentOrder.getDeliveryPersonId() != 0) {
                    Optional<User> deliveryPerson = userService.findUserById(currentOrder.getDeliveryPersonId());
                    if (deliveryPerson.isPresent()) {
                        return ResponseEntity.ok("The delivery person " + deliveryPerson.get().getName() + " " +
                                deliveryPerson.get().getSurname() + " completed the delivery. Would you like to leave a review?");
                    }
                }
            }
            else if (currentOrder.getDeliveryPersonId() == 0) {
                return ResponseEntity.ok("The delivery person is not yet assigned.");
            } else {
                Optional<User> deliveryPerson = userService.findUserById(currentOrder.getDeliveryPersonId());
                if (deliveryPerson.isPresent()) {
                    return ResponseEntity.ok("The delivery person " + deliveryPerson.get().getName() + " " +
                            deliveryPerson.get().getSurname() + " is assigned to your order.");
                }
            }
        }

        return ResponseEntity.notFound().build();
    }


    @GetMapping("/active")
    public ResponseEntity<Boolean> hasActiveOrder(@RequestHeader("Authorization") String token) {
        String jwtToken = token.replace("Bearer ", "");
        String userEmail = jwtUtil.extractEmail(jwtToken);
        Optional<User> user = userService.findUserByEmail(userEmail);
        if (user.isPresent()) {
            List<Order> activeOrders = orderService.findOrdersByUserIdAndIsFinished(user.get().getId(), false);
            return ResponseEntity.ok(!activeOrders.isEmpty());
        } else {
            return ResponseEntity.status(403).build();
        }
    }
    @GetMapping("/activeOrders")
    public ResponseEntity<List<Order>> getAllActiveOrders(@RequestHeader("Authorization") String token) {
        String jwtToken = token.replace("Bearer ", "");
        String userEmail = jwtUtil.extractEmail(jwtToken);
        Optional<User> user = userService.findUserByEmail(userEmail);

        if (user.isPresent()) {
            List<Order> activeOrders = orderService.findAllActiveOrders();
            return ResponseEntity.ok(activeOrders);
        } else {
            return ResponseEntity.status(403).build();
        }
    }

    @GetMapping("/history")
    public ResponseEntity<List<Order>> getOrderHistoryForDeliveryPerson(@RequestHeader("Authorization") String token) {
        String jwtToken = token.replace("Bearer ", "");
        String userEmail = jwtUtil.extractEmail(jwtToken);
        Optional<User> deliveryPerson = userService.findUserByEmail(userEmail);
        if (deliveryPerson.isPresent()) {
            List<Order> orderHistory = orderService.findFinishedOrdersByDeliveryPersonId(deliveryPerson.get().getId());
            return ResponseEntity.ok(orderHistory);
        } else {
            return ResponseEntity.status(403).build();
        }
    }

    @PostMapping("/accept/{orderId}")
    public ResponseEntity<String> acceptOrder(@RequestHeader("Authorization") String token, @PathVariable Long orderId) {
        String jwtToken = token.replace("Bearer ", "");
        String userEmail = jwtUtil.extractEmail(jwtToken);
        Optional<User> deliveryPerson = userService.findUserByEmail(userEmail);

        if (deliveryPerson.isPresent()) {
            Optional<Order> orderOptional = orderService.findOrderById(orderId);
            if (orderOptional.isPresent()) {
                Order order = orderOptional.get();
                if (order.getDeliveryPersonId() == 0) {
                    order.setDeliveryPersonId(deliveryPerson.get().getId());
                    orderService.update(order);
                    String sb = deliveryPerson.get().getName() +
                            " " +
                            deliveryPerson.get().getSurname();
                    Optional<User> user = userService.findUserById(order.getUserId());
                    if(user.isEmpty())
                    {
                        return ResponseEntity.badRequest().body("User does not exist");
                    }
                    emailService.sendOrderAcceptedEmail(user.get().getEmail(), sb);
                    return ResponseEntity.ok("Order accepted by " + deliveryPerson.get().getName());
                } else {
                    return ResponseEntity.badRequest().body("Order is already assigned.");
                }
            }
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.status(403).build();
        }
    }
    @PutMapping("/finish/{orderId}")
    public ResponseEntity<String> markOrderAsFinished(@RequestHeader("Authorization") String token, @PathVariable Long orderId) {
        String jwtToken = token.replace("Bearer ", "");
        String userEmail = jwtUtil.extractEmail(jwtToken);
        Optional<User> deliveryPerson = userService.findUserByEmail(userEmail);

        if (deliveryPerson.isPresent()) {
            Optional<Order> orderOptional = orderService.findOrderById(orderId);
            if (orderOptional.isPresent()) {
                Order order = orderOptional.get();
                if (order.getDeliveryPersonId().equals(deliveryPerson.get().getId())) {
                    order.setFinished(true);
                    orderService.update(order);
                    return ResponseEntity.ok("Order has been marked as finished.");
                } else {
                    return ResponseEntity.status(403).body("You are not authorized to finish this order.");
                }
            } else {
                return ResponseEntity.notFound().build();
            }
        } else {
            return ResponseEntity.status(403).build();
        }
    }
    @PutMapping("/{orderId}/review")
    public ResponseEntity<Map<String, String>> submitReview(
            @RequestHeader("Authorization") String token,
            @PathVariable Long orderId,
            @RequestBody ReviewRequestDTO reviewRequest) {

        String jwtToken = token.replace("Bearer ", "");
        String userEmail = jwtUtil.extractEmail(jwtToken);
        Optional<User> user = userService.findUserByEmail(userEmail);

        if (user.isPresent()) {
            Optional<Order> orderOptional = orderService.findOrderById(orderId);
            if (orderOptional.isPresent()) {
                Order order = orderOptional.get();

                if (order.getUserId().equals(user.get().getId()) && order.isFinished()) {
                    order.setReview(reviewRequest.getReview());
                    order.setRating(reviewRequest.getRating());
                    orderService.update(order);

                    Map<String, String> response = new HashMap<>();
                    response.put("message", "Review and rating submitted successfully.");
                    return ResponseEntity.ok(response);
                } else {
                    return ResponseEntity.status(403).body(null);
                }
            } else {
                return ResponseEntity.notFound().build();
            }
        } else {
            return ResponseEntity.status(403).build();
        }
    }
    @GetMapping("/finished")
    public ResponseEntity<List<Map<String, Object>>> getFinishedOrders(@RequestHeader("Authorization") String token) {
        String jwtToken = token.replace("Bearer ", "");
        String userEmail = jwtUtil.extractEmail(jwtToken);
        Optional<User> user = userService.findUserByEmail(userEmail);

        if (user.isPresent()) {
            List<Order> finishedOrders = orderService.findOrdersByUserIdAndIsFinished(user.get().getId(), true);

            List<Map<String, Object>> orderDetails = finishedOrders.stream().map(order -> {
                Map<String, Object> orderMap = new HashMap<>();
                orderMap.put("id", order.getId());
                orderMap.put("address", order.getAddress());
                orderMap.put("review", order.getReview());
                orderMap.put("rating", order.getRating());

                Optional<User> deliveryPerson = userService.findUserById(order.getDeliveryPersonId());
                if (deliveryPerson.isPresent()) {
                    orderMap.put("deliveryPersonName", deliveryPerson.get().getName());
                    orderMap.put("deliveryPersonSurname", deliveryPerson.get().getSurname());
                }

                return orderMap;
            }).toList();

            return ResponseEntity.ok(orderDetails);
        } else {
            return ResponseEntity.status(403).build();
        }
    }


}
