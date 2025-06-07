package com.cts.wealthmanagementsystem.service;



import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cts.wealthmanagementsystem.dao.ChoosenPlan;
import com.cts.wealthmanagementsystem.entity.Client;
import com.cts.wealthmanagementsystem.entity.InvestmentPlan;
import com.cts.wealthmanagementsystem.repository.InvestmentRepository;



@Service
public class InvestmentPlanServiceImplementation implements InvestmentPlanService {

	@Autowired
	public InvestmentRepository investmentRepository;
    @Override
    public void addInvestmentPlan(InvestmentPlan investmentPlan) {
        // Save to DB or perform persistence logic
    	investmentRepository.save(investmentPlan);
        System.out.println("Saving investment plan: " + investmentPlan);
    }

    @Override
    public void processInvestmentSummary(List<ChoosenPlan> choosenPlans, InvestmentPlan investmentPlan, Client loggedInUser) {
        int stockAmount = 0;
        int mutualFundAmount = 0;
        int goldAmount = 0;

        for (ChoosenPlan cp : choosenPlans) {
            switch (cp.getPlanType()) {
                case "Stock" -> stockAmount += cp.getEntryPrice();
                case "Gold" -> goldAmount += cp.getEntryPrice();
                default -> mutualFundAmount += cp.getEntryPrice();
            }
        }

        int totalAmount = stockAmount + mutualFundAmount + goldAmount;

        int stockPercentage = (totalAmount != 0) ? stockAmount * 100 / totalAmount : 0;
        int goldPercentage = (totalAmount != 0) ? goldAmount * 100 / totalAmount : 0;
        int mutualFundPercentage = (totalAmount != 0) ? mutualFundAmount * 100 / totalAmount : 0;

        investmentPlan.setClientId(String.valueOf(loggedInUser.getIdentity()));
        investmentPlan.setAllocationDetails(
            "stock " + stockPercentage + "% gold " + goldPercentage + "% mutual fund " + mutualFundPercentage + "%"
        );

        addInvestmentPlan(investmentPlan);
    }

	@Override
	public boolean isPlanValidForRisk(String riskAppetite, double entryPrice) {
		// TODO Auto-generated method stub


if (riskAppetite == null) return false;
switch (riskAppetite.toLowerCase()) {
 case "low":
return entryPrice < 3000;
case "medium":
return entryPrice >= 3000 && entryPrice < 10000;
case "high":
return entryPrice >= 10000;
default:
return false;

	}
}
	
	
	@Override
	public Map<String, Integer> calculateInvestmentBreakdown(List<ChoosenPlan> choosenPlans) {
	    int stockAmount = 0;
	    int mutualFundAmount = 0;
	    int goldAmount = 0;

	    for (ChoosenPlan cp : choosenPlans) {
	        switch (cp.getPlanType()) {
	            case "Stock" -> stockAmount += cp.getEntryPrice();
	            case "Gold" -> goldAmount += cp.getEntryPrice();
	            default -> mutualFundAmount += cp.getEntryPrice();
	        }
	    }

	    Map<String, Integer> breakdown = new HashMap();
	    breakdown.put("Stock", stockAmount);
	    breakdown.put("Gold", goldAmount);
	    breakdown.put("MutualFund", mutualFundAmount);

	    return breakdown;
	}

}

