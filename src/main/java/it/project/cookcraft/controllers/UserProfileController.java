package it.project.cookcraft.controllers;

import it.project.cookcraft.dto.UserDTO;
import it.project.cookcraft.security.JwtUtil;
import it.project.cookcraft.services.interfaces.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class UserProfileController {
    private final UserService userService;
    private final JwtUtil jwtUtil;

    public UserProfileController(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @GetMapping("/profile")
    public ResponseEntity<Map<String, String>> getUserProfile(@RequestHeader("Authorization") String token) {
        String email = jwtUtil.extractEmail(token.substring(7));
        return userService.findUserByEmail(email)
                .map(user -> {
                    Map<String, String> profile = Map.of(
                            "userName", user.getName(),
                            "userSurname", user.getSurname()
                    );
                    return new ResponseEntity<>(profile, HttpStatus.OK);
                })
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/profile/update")
    public ResponseEntity<Map<String, String>> updateUserProfile(
            @RequestHeader("Authorization") String token,
            @RequestBody UserDTO userDTO) {
        userService.updateUser(userDTO);
        Map<String, String> response = Map.of("message", "Profile updated successfully");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
