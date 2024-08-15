package it.project.cookcraft.controllers;

import it.project.cookcraft.models.User;
import it.project.cookcraft.models.UserType;
import it.project.cookcraft.services.interfaces.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class RegisterController {

    private final UserService userService;

    public RegisterController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody User user) {
        if (userService.findUserByEmail(user.getEmail()).isPresent()){
            return new ResponseEntity<>("Email already registered", HttpStatus.BAD_REQUEST);
        }
        if (user.getUserType() == null) {
            user.setUserType(UserType.User);
        }
        userService.saveUser(user);
        return new ResponseEntity<>("User registered successfully", HttpStatus.OK);
    }
}
