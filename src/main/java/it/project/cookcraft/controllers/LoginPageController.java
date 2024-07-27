package it.project.cookcraft.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginPageController {

    @RequestMapping("/login")
    public String testLogin() {
        return "Test Kikio";
    }
}
