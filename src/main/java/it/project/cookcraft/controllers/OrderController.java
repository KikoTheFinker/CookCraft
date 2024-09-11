package it.project.cookcraft.controllers;

import it.project.cookcraft.models.*;
import it.project.cookcraft.security.JwtUtil;
import it.project.cookcraft.services.interfaces.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;
    private final ProductOrderService productOrderService;
    private final ProductService productService;
    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final DeliveryPersonService deliveryPersonService;

    public OrderController(OrderService orderService, ProductOrderService productOrderService,
                           ProductService productService, UserService userService, JwtUtil jwtUtil, DeliveryPersonService deliveryPersonService) {
        this.orderService = orderService;
        this.productOrderService = productOrderService;
        this.productService = productService;
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.deliveryPersonService = deliveryPersonService;
    }

    @PostMapping
    public ResponseEntity<Order> saveOrder(@RequestHeader("Authorization") String token, @RequestBody Order order) {
        String jwtToken = token.replace("Bearer ", "");
        String userEmail = jwtUtil.extractEmail(jwtToken);
        Optional<User> user = userService.findUserByEmail(userEmail);

        if (user.isPresent()) {
            order.setUserId(user.get().getId());
        } else {
            return ResponseEntity.badRequest().body(null);
        }

        orderService.saveOrder(order);
        return ResponseEntity.ok(order);
    }

    //TODO fix this bro im confused
    @GetMapping("/{id}")
    public ResponseEntity<Order> findOrderById(@PathVariable Long id) {
        return orderService.findOrderById(id)
                .map(order -> {
                    List<ProductOrder> productOrders = productOrderService.findProductOrdersByOrderId(order.getId());
                    List<Product> products = productOrders.stream()
                            .map(productOrder -> {
                                Product product = productService.findProductById(productOrder.getProductId()).orElse(null);
                                if (product != null) {
                                    product.setQuantity(productOrder.getQuantity());
                                }
                                return product;
                            })
                            .collect(Collectors.toList());
//                    order.setProductOrders(products);
                    return ResponseEntity.ok(order);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    //TODO fix this too pls
    @GetMapping("/active")
    public ResponseEntity<List<Order>> getActiveOrders(@RequestHeader("Authorization") String token) {
        String jwtToken = token.replace("Bearer ", "");
        String userEmail = jwtUtil.extractEmail(jwtToken);
        Optional<User> user = userService.findUserByEmail(userEmail);

        if (user.isPresent()) {
            List<Order> activeOrders = productOrderService.findProductOrdersByDeliveryPersonId(null).stream()
                    .map(ProductOrder::getOrderId)
                    .distinct()
                    .map(orderService::findOrderById)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .map(order -> {
                        List<ProductOrder> productOrders = productOrderService.findProductOrdersByOrderId(order.getId());
                        List<Product> products = productOrders.stream()
                                .map(productOrder -> {
                                    Product product = productService.findProductById(productOrder.getProductId()).orElse(null);
                                    if (product != null) {
                                        product.setQuantity(productOrder.getQuantity()); // Set quantity from ProductOrder
                                    }
                                    return product;
                                })
                                .collect(Collectors.toList());
                        //order.setProducts(products);
                        return order;
                    })
                    .collect(Collectors.toList());

            return ResponseEntity.ok(activeOrders);
        } else {
            return ResponseEntity.status(403).build();
        }
    }

    //TODO this too bro the commented part
    @GetMapping("/history")
    public ResponseEntity<List<Order>> getOrderHistory(@RequestHeader("Authorization") String token) {
        String jwtToken = token.replace("Bearer ", "");
        String userEmail = jwtUtil.extractEmail(jwtToken);
        Optional<User> user = userService.findUserByEmail(userEmail);

        if (user.isPresent()) {
            Long userId = user.get().getId();
            List<Order> orderHistory = productOrderService.findProductOrdersByDeliveryPersonId(userId).stream()
                    .map(ProductOrder::getOrderId)
                    .distinct()
                    .map(orderService::findOrderById)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(Collectors.toList());

            for (Order order : orderHistory) {
                List<Product> products = productOrderService.findProductOrdersByOrderId(order.getId()).stream()
                        .map(productOrder -> {
                            Product product = productService.findProductById(productOrder.getProductId()).orElse(null);
                            if (product != null) {
                                product.setQuantity(productOrder.getQuantity()); // Set quantity from ProductOrder
                            }
                            return product;
                        })
                        .collect(Collectors.toList());
                //order.setProducts(products);
            }

            return ResponseEntity.ok(orderHistory);
        } else {
            return ResponseEntity.status(403).build();
        }
    }

    @PostMapping("/accept/{orderId}")
    public ResponseEntity<String> acceptOrder(@PathVariable Long orderId, @RequestHeader("Authorization") String token) {
        String jwtToken = token.replace("Bearer ", "");
        String userEmail = jwtUtil.extractEmail(jwtToken);
        Optional<User> user = userService.findUserByEmail(userEmail);

        if (user.isPresent()) {
            Long deliveryPersonId = user.get().getId();
            Optional<User> deliveryPerson = userService.findDeliveryPersonByUserId(deliveryPersonId);
            if (deliveryPerson.isEmpty()) {
                return ResponseEntity.badRequest().body("Invalid delivery person.");
            }

            List<ProductOrder> productOrders = productOrderService.findProductOrdersByOrderId(orderId);
            for (ProductOrder productOrder : productOrders) {
                //TODO need to delete this aswell
                //productOrder.setDeliveryPersonId(deliveryPersonId);
                productOrderService.saveProductOrder(productOrder);

                Optional<DeliveryPerson> deliveryPersonRecord = deliveryPersonService.findByUserId(deliveryPersonId);
                if (deliveryPersonRecord.isPresent()) {
                    DeliveryPerson dp = deliveryPersonRecord.get();
                    dp.setProductOrderId(productOrder.getId());
                    deliveryPersonService.saveDeliveryPerson(dp);
                } else {
                    return ResponseEntity.badRequest().body("Delivery person not found.");
                }
            }
            return ResponseEntity.ok("Order accepted successfully.");
        } else {
            return ResponseEntity.status(403).body("Unauthorized access.");
        }
    }
}
