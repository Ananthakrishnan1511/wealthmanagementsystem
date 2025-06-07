package com.cts.wealthmanagementsystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import com.cts.wealthmanagementsystem.dao.ChoosenPlan;
import com.cts.wealthmanagementsystem.entity.PortfolioItem;
import com.cts.wealthmanagementsystem.service.PortfolioService;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects; // Make sure this is imported

@Controller
@SessionAttributes({"totalInvestment", "goldBuyValue", "mutualFundsBuyValue", "shareMarketBuyValue","portfolioItems"})
public class PortfolioController {

    @Autowired
    private PortfolioService portfolioService;

  
    
    @ModelAttribute("portfolioItems")
    public List<PortfolioItem> portfolioItem() {
        return new ArrayList<>();
    }
    @GetMapping("/view") 
    public String home(Model model,@ModelAttribute("portfolioItems") List<PortfolioItem> portfolioItems) {
        System.out.println("PortfolioController.home() method called.");

        double goldBuyValue = ((Number) Objects.requireNonNullElse(model.getAttribute("goldBuyValue"), 0.0)).doubleValue();
        double mutualFundsBuyValue = ((Number) Objects.requireNonNullElse(model.getAttribute("mutualFundsBuyValue"), 0.0)).doubleValue();
        double shareMarketBuyValue = ((Number) Objects.requireNonNullElse(model.getAttribute("shareMarketBuyValue"), 0.0)).doubleValue();
        double totalInvestment = ((Number) Objects.requireNonNullElse(model.getAttribute("totalInvestment"), 0.0)).doubleValue();

        if (totalInvestment == 0.0 && goldBuyValue == 0.0 && mutualFundsBuyValue == 0.0 && shareMarketBuyValue == 0.0) {
            System.out.println("No investment data found in session. Displaying empty portfolio.");
            model.addAttribute("message", "Please select an investment plan from the planning module to view your portfolio.");
        }
        model.addAttribute("portfolioItems",portfolioItems);
        // Get portfolio details and rebalancing ideas from the service using the current session values
        List<PortfolioItem> portfolioItems1 = portfolioService.getPortfolio(goldBuyValue, mutualFundsBuyValue, shareMarketBuyValue,portfolioItems);
        List<String> rebalancingIdeas = portfolioService.getRebalancingIdeas(portfolioItems1);

        // Add all necessary data to the model for Thymeleaf rendering
        model.addAttribute("portfolioItems", portfolioItems1);
        model.addAttribute("rebalancingIdeas", rebalancingIdeas);
        model.addAttribute("totalCurrentValue", portfolioItems1.stream().mapToDouble(PortfolioItem::getCurrentValue).sum());
        model.addAttribute("totalProfitLoss", portfolioItems1.stream().mapToDouble(PortfolioItem::getProfitLoss).sum());

        System.out.println("Portfolio Home Page Model Attributes: " + model);
        return "portfolio"; // Return the name of the Thymeleaf template
    }

    @GetMapping("/displayPortfolioFromPlan")
    public String displayPortfolioFromPlan(
            @RequestParam("totalInv") double totalInvestment,
            @RequestParam("goldVal") double goldBuyValue,
            @RequestParam("mfVal") double mutualFundsBuyValue,
            @RequestParam("smVal") double shareMarketBuyValue,
            Model model) {

        model.addAttribute("totalInvestment", totalInvestment);
        model.addAttribute("goldBuyValue", goldBuyValue);
        model.addAttribute("mutualFundsBuyValue", mutualFundsBuyValue);
        model.addAttribute("shareMarketBuyValue", shareMarketBuyValue);

        System.out.println("Received from Planning Module and stored in session:");
        System.out.println("Total: " + totalInvestment + ", Gold: " + goldBuyValue +
                           ", MF: " + mutualFundsBuyValue + ", SM: " + shareMarketBuyValue);

        return "redirect:/view";
    }

    @GetMapping("/clearSession")
    public String clearSession(SessionStatus status) {
        status.setComplete(); 
        System.out.println("Session attributes cleared.");
       
        return "redirect:/view";
    }
}