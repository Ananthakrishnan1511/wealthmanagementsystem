package com.cts.wealthmanagementsystem.controller;
 
import org.springframework.beans.factory.annotation.Autowired;

import com.cts.wealthmanagementsystem.entity.Client;
import com.cts.wealthmanagementsystem.service.ClientService;

import jakarta.servlet.http.HttpSession;
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
                          PasswordEncoder passwordEncoder) { // Keep PasswordEncoder for consistency or future use
        this.clientService = clientService;
        this.passwordEncoder = passwordEncoder; // Assigned, but not directly used for encoding in this class
    }
    @GetMapping("/profile")
    public String getProfile(Model model, HttpSession session) {
       
        Client newClient = (Client) session.getAttribute("loggedInUser");
       System.out.println(newClient);
        if (newClient != null) {
            model.addAttribute("advisor", newClient); 
            return "profile"; 
            }
        return "redirect:/auth/login"; 
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login"; // Renders login.html
    }
    @GetMapping("/main") 
    public String mainPage() {
        return "main"; 
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
            // Field name "age" matches Client.age
            result.rejectValue("age", "client.age.null", "Age cannot be empty.");
        }  
        // Example: Check for 'fullName' length
        if (client.getFullName() != null && client.getFullName().length() < 2) {
            
            result.rejectValue("fullName", "client.fullName.tooShort", "Full Name must be at least 2 characters.");
        }
 
        if (client.getEmailAddress() != null && !client.getEmailAddress().endsWith(".com")) {
           
            result.rejectValue("emailAddress", "client.email.invalidDomain", "Email must be from s.com domain.");
        }
 
        if (result.hasFieldErrors("passWord")) {
            FieldError passwordError = result.getFieldError("passWord");
            System.out.println("Password validation error detected by @Valid: " + passwordError.getDefaultMessage());
        }
 
        if (client.getUserName() != null && client.getUserName().contains("admin")) {
            // Field name "userName" matches Client.userName
            result.rejectValue("userName", "client.userName.forbidden", "Username cannot contain 'admin'.");
        }
 
        // --- End Custom/Additional Attribute-Specific Checks ---
 
        // 1. Check for any validation errors (from @Valid annotations AND any manual additions above)
        if (result.hasErrors()) {
            return "signup"; // Return to the signup form. Thymeleaf will automatically pick up the error messages.
        }
 
        // 2. Check if email already exists (existing logic, retained)
        if (clientService.existsByEmail(client.getEmailAddress())) {
            model.addAttribute("errorMessage", "An account with this email already exists.");
            return "signup";
        }
 
        client.setApproved(false);
        clientService.addClient(client);
 
        return "redirect:/auth/login?success";
    }
    @GetMapping("/login/editprofile")
    public String editProfilePage(Model model) {
        model.addAttribute("advisor", new Client()); // Ensure Advisor model exists
        return "edit-profile";
    }

    
}
 