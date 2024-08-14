package it.project.cookcraft.controllers;

import it.project.cookcraft.security.JwtUtil;
import it.project.cookcraft.services.interfaces.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api")
public class LoginPageController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    public LoginPageController(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam String email, @RequestParam String password) {
        return userService.findUserByEmail(email)
                .filter(user -> user.getPassword().equals(password))
                .map(user -> {
                    String token = jwtUtil.generateToken(user.getEmail()); // Generate JWT
                    return new ResponseEntity<>(token, HttpStatus.OK); // Return JWT
                })
                .orElseGet(() -> new ResponseEntity<>("Invalid email or password", HttpStatus.OK));
    }
}
