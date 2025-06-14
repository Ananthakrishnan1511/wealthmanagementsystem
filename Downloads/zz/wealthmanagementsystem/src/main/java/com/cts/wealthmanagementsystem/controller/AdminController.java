package com.cts.wealthmanagementsystem.controller;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cts.wealthmanagementsystem.entity.Client;
import com.cts.wealthmanagementsystem.service.ClientService;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final ClientService clientService;

    public AdminController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping("/logout")
    public String gete()
    {
        return "login";
    }
    @GetMapping("/dashboard")
    public String viewDraftUsers(Model model) {
        model.addAttribute("draftUsers", clientService.getAllDraftUsers());
        return "admin-dashboard";
    }

    @GetMapping("/users")
    public String showApprovedClients(Model model) {
        List<Client> approvedClients = clientService.getApprovedClients();
        model.addAttribute("clients", approvedClients);
        return "users";
    }

    @PostMapping("/approve/{identity}")
    public String approveUser(@PathVariable Integer identity) {
        clientService.approveUser(identity);
        return "redirect:/admin/dashboard";
    }

    @PostMapping("/reject/{identity}")
    public String rejectUser(@PathVariable Integer identity) {
        clientService.rejectUser(identity);
        return "redirect:/admin/dashboard";
    }
}