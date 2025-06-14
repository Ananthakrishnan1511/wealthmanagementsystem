package com.cts.wealthmanagementsystem.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.cts.wealthmanagementsystem.dao.ChoosenPlan;
import com.cts.wealthmanagementsystem.entity.Client; // Make sure this import is correct
import com.cts.wealthmanagementsystem.service.ClientService; // Make sure this import is correct

import jakarta.servlet.http.HttpSession;

@Controller
public class ClientController {

    @Autowired
    private ClientService clientService;
    @GetMapping("/summary")
    public String showChosenPlans(@ModelAttribute("choosenPlans") List<ChoosenPlan> choosenPlans, Model model) {
        model.addAttribute("plans", choosenPlans);
 
        double totalInvestment = choosenPlans.stream()
                                         .mapToDouble(ChoosenPlan::getEntryPrice)
                                         .sum();
        model.addAttribute("totalInvestment", totalInvestment);
 
        return "choosenplan";
    }
    @GetMapping("/")
    public String rootPage() {
        return "redirect:/auth/login";
    }

    @GetMapping("/main") 
    public String mainPage() {
        return "main"; 
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

    @PostMapping("/update-profile")
    public String updateProfile(@ModelAttribute Client updatedClient, HttpSession session) {
        Client newClient = (Client) session.getAttribute("loggedInUser"); 

        if (newClient != null) {
            updatedClient.setIdentity(newClient.getIdentity()); 
            updatedClient.setEmailAddress(newClient.getEmailAddress()); 
            clientService.addClient(updatedClient); 
            session.setAttribute("loggedInUser", updatedClient);
            System.out.println("correct password");
            return "redirect:/main"; 
        }
        System.out.println("Incorrect password");
        return "redirect:/auth/login";
    }
}