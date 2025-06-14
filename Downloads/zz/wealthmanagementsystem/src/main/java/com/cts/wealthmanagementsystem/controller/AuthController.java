package com.cts.wealthmanagementsystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
// AuthenticationManager and related imports are not strictly needed in the AuthController
// if you're letting Spring Security handle the form login automatically.
// import org.springframework.security.authentication.AuthenticationManager;
// import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
// import org.springframework.security.core.Authentication;
// import org.springframework.security.core.userdetails.UserDetails;
// import jakarta.servlet.http.HttpServletRequest;
// import jakarta.servlet.http.HttpSession;

import com.cts.wealthmanagementsystem.entity.Client;
import com.cts.wealthmanagementsystem.service.ClientService;

import jakarta.validation.Valid;

// import jakarta.servlet.http.HttpSession; // Duplicate import

import org.springframework.security.crypto.password.PasswordEncoder; // Still needed for dependency injection
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/auth")
public class AuthController {

    private final ClientService clientService;
    private final PasswordEncoder passwordEncoder; 
    @Autowired
    public AuthController(ClientService clientService,
                          PasswordEncoder passwordEncoder) { 
        this.clientService = clientService;
        this.passwordEncoder = passwordEncoder; 
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login"; 
    }

    @GetMapping("/signup")
    public String signUpPage(Model model) {
        model.addAttribute("client", new Client());
        return "signup";
    }

    @PostMapping("/signup/saveEmployee")
    public String registerUser(@Valid @ModelAttribute("client") Client client,
                               BindingResult result,
                               Model model) {

        if (client.getAge() == null) {
            result.rejectValue("age", "client.age.null", "Age cannot be empty.");
        } else if (client.getAge() < 18) {
            result.rejectValue("age", "client.age.underAge", "You must be at least 18 years old to register.");
        }

        if (client.getFullName() != null && client.getFullName().length() < 2) {
            result.rejectValue("fullName", "client.fullName.tooShort", "Full Name must be at least 2 characters.");
        }

        if (client.getEmailAddress() != null && !client.getEmailAddress().endsWith("@gmail.com")) {
            result.rejectValue("emailAddress", "client.email.invalidDomain", "Email must be from @gmail.com domain.");
        }

        if (result.hasFieldErrors("passWord")) {
            FieldError passwordError = result.getFieldError("passWord");
            System.out.println("Password validation error detected by @Valid: " + passwordError.getDefaultMessage());
        }

        if (client.getUserName() != null && client.getUserName().contains("admin")) {
            result.rejectValue("userName", "client.userName.forbidden", "Username cannot contain 'admin'.");
        }

        if (result.hasErrors()) {
            return "signup"; 
        }

        if (clientService.existsByEmail(client.getEmailAddress())) {
            model.addAttribute("errorMessage", "An account with this email already exists.");
            return "signup";
        }

        client.setApproved(false);
        clientService.addClient(client);

        return "redirect:/auth/login?success";
    }
}