package com.cts.wealthmanagementsystem.service;


import com.cts.wealthmanagementsystem.dao.ChoosenPlan;
import com.cts.wealthmanagementsystem.entity.Client;
import com.cts.wealthmanagementsystem.entity.InvestmentPlan;

import java.util.List;
import java.util.Map;

public interface InvestmentPlanService {
    void addInvestmentPlan(InvestmentPlan investmentPlan);
    void processInvestmentSummary(List<ChoosenPlan> choosenPlans, InvestmentPlan investmentPlan, Client loggedInUser);
    boolean isPlanValidForRisk(String riskAppetite, double entryPrice);
    Map<String, Integer> calculateInvestmentBreakdown(List<ChoosenPlan> choosenPlans);

}

