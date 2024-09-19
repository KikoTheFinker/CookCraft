package it.project.cookcraft.controllers;

import it.project.cookcraft.models.Application;
import it.project.cookcraft.models.User;
import it.project.cookcraft.services.interfaces.ApplicationService;
import it.project.cookcraft.services.interfaces.UserService;
import it.project.cookcraft.security.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class ApplicationController {

    private final ApplicationService applicationService;
    private final UserService userService;
    private final JwtUtil jwtUtil;

    public ApplicationController(ApplicationService applicationService, UserService userService, JwtUtil jwtUtil) {
        this.applicationService = applicationService;
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/apply")
    public ResponseEntity<?> apply(
            @RequestParam("cv") MultipartFile cv,
            @RequestParam("motivational_letter") String motivational_letter,
            @RequestParam("phone_number") String phoneNumber,
            @RequestParam("email") String email,
            @RequestHeader("Authorization") String tokenHeader
            ) {
        try {
            String token = tokenHeader.substring(7);
            String userEmail = jwtUtil.extractEmail(token);

            User user = userService.findUserByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            Long userId = user.getId();


            Application application = new Application();
            application.setMotivationalLetter(motivational_letter);
            application.setUserId(userId);
            application.setCv(cv.getBytes());

            applicationService.saveApplication(application);
            return new ResponseEntity<>("Application created successfully", HttpStatus.CREATED);
        }
        catch (Exception e) {
            return new ResponseEntity<>("Error creating application: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
