package it.project.cookcraft.controllers;

import it.project.cookcraft.models.Application;
import it.project.cookcraft.models.User;
import it.project.cookcraft.services.interfaces.ApplicationService;
import it.project.cookcraft.services.interfaces.UserService;
import it.project.cookcraft.security.JwtUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<String> createApplication(@RequestBody Map<String, Object> applicationData, @RequestHeader("Authorization") String tokenHeader) {
        try {
            String token = tokenHeader.substring(7);
            String userEmail = jwtUtil.extractEmail(token);

            User user = userService.findUserByEmail(userEmail)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            Long userId = user.getId();

            Application application = new Application();
            application.setUserId(userId);
            application.setMotivationalLetter((String) applicationData.get("motivational_letter"));
            application.setCv(applicationData.get("cv") != null ? ((String) applicationData.get("cv")).getBytes() : null);

            applicationService.saveApplication(application);

            return new ResponseEntity<>("Application created successfully", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Error creating application: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
