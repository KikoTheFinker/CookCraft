package it.project.cookcraft.controllers;

import it.project.cookcraft.models.User;
import it.project.cookcraft.models.UserType;
import it.project.cookcraft.security.JwtUtil;
import it.project.cookcraft.services.interfaces.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class DeliveryController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    public DeliveryController(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
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

    }
}
