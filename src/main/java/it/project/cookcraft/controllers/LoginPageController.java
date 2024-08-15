package it.project.cookcraft.controllers;

import it.project.cookcraft.security.JwtUtil;
import it.project.cookcraft.services.interfaces.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
                    Map<String, String> response = Map.of(
                            "token", token,
                            "user_name", user.getName(),
                            "user_surname", user.getSurname()
                    );
                    return new ResponseEntity<>(response, HttpStatus.OK); // Return JWT and user details in a JSON response
                })
                .orElseGet(() -> new ResponseEntity<>(Map.of("error", "Invalid email or password"), HttpStatus.UNAUTHORIZED));
    }
}
