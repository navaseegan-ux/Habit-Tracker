package com.example.project1.Habit.Tracker.controller;

import com.example.project1.Habit.Tracker.service.AppUserService;
import com.example.project1.Habit.Tracker.service.RegistrationResult;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {

    private final AppUserService appUserService;

    public AuthController(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String register(Model model) {
        populateRegisterModel(model, "", "");
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(
        @RequestParam String username,
        @RequestParam String email,
        @RequestParam String password,
        Model model
    ) {
        RegistrationResult result = appUserService.registerUser(username, email, password);

        if (result.success()) {
            return "redirect:/login?registered";
        }

        model.addAttribute("errorMessage", result.message());
        populateRegisterModel(model, username, email);
        return "register";
    }

    private void populateRegisterModel(Model model, String username, String email) {
        model.addAttribute("username", username == null ? "" : username.trim());
        model.addAttribute("email", email == null ? "" : email.trim());
    }
}
