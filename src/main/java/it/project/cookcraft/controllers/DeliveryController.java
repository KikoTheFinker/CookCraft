package it.project.cookcraft.controllers;

import it.project.cookcraft.models.Order;
import it.project.cookcraft.models.User;
import it.project.cookcraft.models.UserType;
import it.project.cookcraft.security.JwtUtil;
import it.project.cookcraft.services.interfaces.OrderService;
import it.project.cookcraft.services.interfaces.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class DeliveryController {

    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final OrderService orderService;

    public DeliveryController(UserService userService, JwtUtil jwtUtil, OrderService orderService) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.orderService = orderService;
    }

    @GetMapping("/deliver")
    public ResponseEntity<String> startDelivering(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);

        String email = jwtUtil.extractEmail(token);

        User user = userService.findUserByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getUserType() == UserType.DeliveryPerson) {
            return ResponseEntity.ok("Welcome to the delivery dashboard!");
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
        }
    }@GetMapping("/deliveryperson/ongoing")
    public ResponseEntity<Order> getOngoingOrderForDeliveryPerson(@RequestHeader("Authorization") String token) {
        String jwtToken = token.replace("Bearer ", "");
        String userEmail = jwtUtil.extractEmail(jwtToken);
        Optional<User> deliveryPerson = userService.findUserByEmail(userEmail);

        if (deliveryPerson.isPresent()) {
            List<Order> ongoingOrders = orderService.findOrdersByDeliveryPersonIdAndIsNotFinished(deliveryPerson.get().getId());
            if (!ongoingOrders.isEmpty()) {
                return ResponseEntity.ok(ongoingOrders.get(0));
            } else {
                return ResponseEntity.noContent().build();
            }
        } else {
            return ResponseEntity.status(403).build();
        }
    }


}
