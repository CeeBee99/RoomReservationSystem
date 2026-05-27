package com.example.roomreservationservice.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String showLoginPage() {
        return "login"; // loads login.html
    }

    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password) {

        if (username.equals("admin") && password.equals("admin123")) {
            return "redirect:/admin";
        }

        return "login"; // stay on login page if wrong
    }
}
