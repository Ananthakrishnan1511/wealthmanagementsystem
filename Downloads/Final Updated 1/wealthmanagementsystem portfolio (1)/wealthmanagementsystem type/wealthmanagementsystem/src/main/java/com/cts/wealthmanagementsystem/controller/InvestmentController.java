package com.cts.wealthmanagementsystem.controller;
import org.springframework.ui.Model;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cts.wealthmanagementsystem.entity.InvestmentPlan;

import jakarta.servlet.http.HttpSession;


@Controller
@RequestMapping("/auth")
public class InvestmentController {
	
    @PostMapping("/plan")
    public String getPlan(@ModelAttribute InvestmentPlan investPlan,HttpSession session) {
    	session.setAttribute("investPlan", investPlan);
    	return "plan";
    }
    

    @GetMapping("/gold")
    public String gold() {
    	return "gold";
    }
    
    @GetMapping("/mutualfunds")
    public String mutualfunds() {
    	return "mutualfunds";
    }
    
    @GetMapping("/stocks")
    public String stocks() {
    	return "stocks";
    }
    
    @GetMapping("/savesummary")
    public String showSummary(@ModelAttribute("investmentForm") InvestmentPlan form, Model model) {
        model.addAttribute("objective", form.getInvestmentObjective());
        model.addAttribute("risk", form.getRiskAppetite());
        return "summary"; // summary.html
    }
    

   
}


