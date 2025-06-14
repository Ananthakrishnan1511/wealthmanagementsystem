package com.cts.wealthmanagementsystem.controller;
 
import com.cts.wealthmanagementsystem.dao.ChoosenPlan;
import com.cts.wealthmanagementsystem.entity.AuditLog;
import com.cts.wealthmanagementsystem.entity.Client;
import com.cts.wealthmanagementsystem.entity.InvestmentPlan;

import com.cts.wealthmanagementsystem.service.AuditLogService;
import com.cts.wealthmanagementsystem.service.InvestmentPlanService;

 
import jakarta.servlet.http.HttpSession;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
 
import java.util.ArrayList;
import java.util.List;

@Controller
@SessionAttributes({"choosenPlans","investPlan","loggedInUser","totalInvestment", "goldBuyValue", "mutualFundsBuyValue"})
@RequestMapping("/auth")
public class PlanController {
	@Autowired
	public InvestmentPlanService investmentPlanService ;
 
 
@Autowired
public  AuditLogService auditLogService;
 
	
	 @GetMapping("/investment")
	    public String showInvestmentPage(Model model,HttpSession session) {
    	InvestmentPlan investPlan = new InvestmentPlan();
    	model.addAttribute("investPlan",investPlan);
    	 session.setAttribute("investPlan", investPlan);
	    	System.out.println(investPlan.getRiskAppetite());
	        return "investment";
	    }
 
    @ModelAttribute("choosenPlans")
    public List<ChoosenPlan> choosenPlans() {
        return new ArrayList<>();
    }
 
    @PostMapping("/buy")
    public String buyPlan(@RequestParam String planType,
                          @RequestParam String planName,
                          @RequestParam double entryPrice,
                          @ModelAttribute("choosenPlans") List<ChoosenPlan> choosenPlans,
                          @SessionAttribute("investPlan") InvestmentPlan investPlan,
                          Model model) {
 
        String risk = investPlan.getRiskAppetite();
        boolean isValid = false;
 
        switch (risk.toLowerCase()) {
            case "low":
                isValid = entryPrice < 3000;
                break;
            case "medium":
                isValid = entryPrice >= 3000 && entryPrice < 10000;
                break;
            case "high":
                isValid = entryPrice >= 10000;
                break;
        }
 
        if (!isValid) {
            model.addAttribute("errorMessage", "❌ This plan does not match your selected risk appetite (" + risk + "). Please choose a suitable plan.");
            model.addAttribute("errorPlan", planName);
            String status="FAILED";
            AuditLog auditLogfinal= auditLogService.verifyCompliance(status);
            auditLogService.logAuditEvent(auditLogfinal);
            return planType.equalsIgnoreCase("Mutual Funds") ? "mutualfunds" : "stocks";
        }
 
 
        choosenPlans.add(new ChoosenPlan(planType, planName, entryPrice));
        model.addAttribute("choosenPlans",choosenPlans);
        model.addAttribute("successMessage", "✅ Your plan has been added successfully!");
        model.addAttribute("successPlan", planName); 
        String status="SUCCESS";
        AuditLog auditLogfinal= auditLogService.verifyCompliance(status);
        auditLogService.logAuditEvent(auditLogfinal);
        return planType.equalsIgnoreCase("Mutual Funds") ? "mutualfunds" : "stocks";
    }
 
    @PostMapping("/buy-gold")
    public String buyGold(@RequestParam double amount,
                          @ModelAttribute("choosenPlans") List<ChoosenPlan> choosenPlans,
                          @SessionAttribute("investPlan") InvestmentPlan investPlan,
                          Model model) {
 
        String risk = investPlan.getRiskAppetite();
        boolean isValid = false;
 
        switch (risk.toLowerCase()) {
            case "low":
                isValid = amount < 3000;
                break;
            case "medium":
                isValid = amount >= 3000 && amount < 10000;
                break;
            case "high":
                isValid = amount >= 10000;
                break;
        }
 
        if (!isValid) {
            model.addAttribute("errorMessage", "❌ Your investment amount doesn't match your selected risk appetite (" + risk + "). Please adjust the amount.");
 
            String status="FAILED";
            AuditLog auditLogfinal= auditLogService.verifyCompliance(status);
            auditLogService.logAuditEvent(auditLogfinal);
            
 
            return "gold"; // Return to the gold.html page
        }
 
       
 
 
        double goldPricePerGram = 6263.0;
        double grams = amount / goldPricePerGram;
 
        choosenPlans.add(new ChoosenPlan("Gold", "-", amount));
        model.addAttribute("successMessage", String.format("✅ You invested ₹%.2f in gold (%.2f grams)", amount, grams));
        String status="SUCCESS";
        AuditLog auditLogfinal= auditLogService.verifyCompliance(status);
        auditLogService.logAuditEvent(auditLogfinal);
        
        return "gold";
    }
 
 
    @GetMapping("/summary")
    public String showChosenPlans(@ModelAttribute("choosenPlans") List<ChoosenPlan> choosenPlans, Model model) {
        model.addAttribute("plans", choosenPlans);
 
        double totalInvestment = choosenPlans.stream()
                                         .mapToDouble(ChoosenPlan::getEntryPrice)
                                         .sum();
        model.addAttribute("totalInvestment", totalInvestment);
 
        return "choosenplan";
    }
    @PostMapping("/confirm")
    public String postInDatabase(@ModelAttribute("choosenPlans") List<ChoosenPlan> choosenPlans,
                                 HttpSession session,
                                 @ModelAttribute("loggedInUser") Client loggedInUser,
                                 Model model) {

        int stockAmount = 0;
        int mutualFundAmount = 0;
        int goldAmount = 0;

        for (ChoosenPlan cp : choosenPlans) {
            switch (cp.getPlanType()) {
                case "Stock":
                    stockAmount += cp.getEntryPrice();
                    break;
                case "Gold":
                    goldAmount += cp.getEntryPrice();
                    break;
                default:
                    mutualFundAmount += cp.getEntryPrice();
                    break;
            }
        }

        model.addAttribute("shareMarketBuyValue", stockAmount);
        model.addAttribute("mutualFundsBuyValue", mutualFundAmount);
        model.addAttribute("goldBuyValue", goldAmount);

        InvestmentPlan investmentPlan = (InvestmentPlan) session.getAttribute("investPlan");

        investmentPlanService.processInvestmentSummary(choosenPlans, investmentPlan, loggedInUser);

        return "confirm";
    }

}