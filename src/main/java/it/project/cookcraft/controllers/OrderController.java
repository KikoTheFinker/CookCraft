package it.project.cookcraft.controllers;

import it.project.cookcraft.models.Order;
import it.project.cookcraft.models.ProductOrder;
import it.project.cookcraft.models.User;
import it.project.cookcraft.security.JwtUtil;
import it.project.cookcraft.services.interfaces.OrderService;
import it.project.cookcraft.services.interfaces.ProductOrderService;
import it.project.cookcraft.services.interfaces.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;
    private final ProductOrderService productOrderService;
    private final UserService userService;
    private final JwtUtil jwtUtil;

    public OrderController(OrderService orderService, ProductOrderService productOrderService,
                           UserService userService, JwtUtil jwtUtil) {
        this.orderService = orderService;
        this.productOrderService = productOrderService;
        this.userService = userService;
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
            if (order.get().getDeliveryPersonId() == 0) {
                return ResponseEntity.ok("The delivery person is not yet assigned.");
            } else {
                Optional<User> deliveryPerson = userService.findUserById(order.get().getDeliveryPersonId());
                if (deliveryPerson.isPresent()) {
                    return ResponseEntity.ok("The delivery person "+deliveryPerson.get().getName() + " " +
                            deliveryPerson.get().getSurname()+" is assigned.");
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
}
