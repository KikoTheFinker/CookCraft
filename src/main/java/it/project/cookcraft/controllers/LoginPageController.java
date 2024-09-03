package it.project.cookcraft.controllers;

import it.project.cookcraft.security.JwtUtil;
import it.project.cookcraft.services.interfaces.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class LoginPageController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    public LoginPageController(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody Map<String, String> credentials) {
        String email = credentials.get("email");
        String password = credentials.get("password");

        return userService.findUserByEmail(email)
                .filter(user -> user.getPassword().equals(password))
                .map(user -> {
                    String token = jwtUtil.generateToken(user.getEmail()); // Generate JWT
                    Map<String, String> response = new HashMap<>();
                    response.put("token", token);
                    response.put("user_name", user.getName());
                    response.put("user_surname", user.getSurname());
                    response.put("email", user.getEmail());
                    if(user.getAddress() != null) {
                        response.put("address", user.getAddress());
                    }
                    if (user.getPhoneNumber() != null) {
                        response.put("phone_number", user.getPhoneNumber());
                    }
                    return new ResponseEntity<>(response, HttpStatus.OK);
                })
                .orElseGet(() -> new ResponseEntity<>(Map.of("error", "Invalid email or password"), HttpStatus.UNAUTHORIZED));
    }
}
