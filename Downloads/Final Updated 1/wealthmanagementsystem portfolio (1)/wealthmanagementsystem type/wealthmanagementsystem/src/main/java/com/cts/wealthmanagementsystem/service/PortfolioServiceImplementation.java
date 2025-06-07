package com.cts.wealthmanagementsystem.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.cts.wealthmanagementsystem.entity.PortfolioItem;
import com.cts.wealthmanagementsystem.repository.PortfolioRepository;

@Service

public class PortfolioServiceImplementation implements PortfolioService {
	  @Autowired
			private PortfolioRepository portfolioRepository ;
		  
		 
			public PortfolioItem addPortfolioItem(PortfolioItem portfolioItem) {
				System.out.print(portfolioItem.getProfitLoss());
				return portfolioRepository.save(portfolioItem);
			}
	     

	    
	    private static final double GOLD_CURRENT_VALUE_FACTOR = 1.10; 
	    private static final double MUTUAL_FUNDS_CURRENT_VALUE_FACTOR = 0.95; 
	    private static final double SHARE_MARKET_CURRENT_VALUE_FACTOR = 1.25; 

	   
	    private static final double TARGET_MIN_ALLOCATION_PERCENT = 20.0;
	    private static final double OVER_ALLOCATION_THRESHOLD_PERCENT = 40.0; 
	    private static final double UNDER_ALLOCATION_THRESHOLD_PERCENT = 15.0; 
	    private static final double PROFIT_TAKING_PERCENT = 0.10; 
	    private static final double LOSS_COVERAGE_PERCENT = 0.50;

	   
	    public List<PortfolioItem> getPortfolio(double goldBuyValue, double mutualFundsBuyValue, double shareMarketBuyValue,@ModelAttribute("portfolioItems") List<PortfolioItem> portfolioItems) {
	       

	       
	        double goldCurrentValue = goldBuyValue * GOLD_CURRENT_VALUE_FACTOR;
	        double mutualFundsCurrentValue = mutualFundsBuyValue * MUTUAL_FUNDS_CURRENT_VALUE_FACTOR;
	        double shareMarketCurrentValue = shareMarketBuyValue * SHARE_MARKET_CURRENT_VALUE_FACTOR;

	        
	        portfolioItems.add(new PortfolioItem("Gold", goldBuyValue, goldCurrentValue, 0.0, 0.0));
	        portfolioItems.add(new PortfolioItem("Mutual Funds", mutualFundsBuyValue, mutualFundsCurrentValue, 0.0, 0.0));
	        portfolioItems.add(new PortfolioItem("Share Market", shareMarketBuyValue, shareMarketCurrentValue, 0.0, 0.0));

	        
	        calculateProfitLossAndAllocation(portfolioItems);
	        return portfolioItems;
	    }

	  


	   
	    public void calculateProfitLossAndAllocation(@ModelAttribute("portfolioItems") List<PortfolioItem> portfolioItems) {
	        
	        double totalCurrentValue = portfolioItems.stream()
	                .mapToDouble(PortfolioItem::getCurrentValue)
	                .sum();

	       
	        for (PortfolioItem item : portfolioItems) {
	            double profitLoss = item.getCurrentValue() - item.getBuyValue();
	            item.setProfitLoss(profitLoss);

	            
	            if (totalCurrentValue > 0) {
	                double allocationPercentage = (item.getCurrentValue() / totalCurrentValue) * 100;
	                item.setAllocationPercentage(allocationPercentage);
	            } else {
	                item.setAllocationPercentage(0.0); 
	            }
	            
	            addPortfolioItem(item);
	        }
	        
	    }

	    
	    public List<String> getRebalancingIdeas(List<PortfolioItem> portfolioItems) {
	        List<String> ideas = new ArrayList<>();
	        double totalCurrentValue = portfolioItems.stream().mapToDouble(PortfolioItem::getCurrentValue).sum();

	       
	        List<PortfolioItem> losers = portfolioItems.stream()
	                .filter(item -> item.getProfitLoss() < 0)
	                .sorted(Comparator.comparingDouble(PortfolioItem::getProfitLoss)) 
	                .collect(Collectors.toList());

	        List<PortfolioItem> gainers = portfolioItems.stream()
	                .filter(item -> item.getProfitLoss() >= 0)
	                .sorted(Comparator.comparingDouble(PortfolioItem::getProfitLoss).reversed()) 
	                .collect(Collectors.toList());

	       
	        for (PortfolioItem item : losers) {
	            
	            double suggestedInvestment = Math.abs(item.getProfitLoss()) * LOSS_COVERAGE_PERCENT;
	            if (suggestedInvestment > 0) { 
	                ideas.add(String.format("Your **%s** investment is down. Consider **adding $%.2f** to average down your cost basis. This could help recover losses faster if the asset rebounds.",
	                        item.getName(), suggestedInvestment));
	                if (ideas.size() >= 3) break; 
	            }
	        }

	        
	        for (PortfolioItem item : gainers) {
	            if (item.getAllocationPercentage() > OVER_ALLOCATION_THRESHOLD_PERCENT) {
	                // Suggest taking a percentage of the profit or an amount to reduce allocation
	                double amountToTrim = item.getProfitLoss() * PROFIT_TAKING_PERCENT;
	                // Ensure we don't suggest trimming more than the current value or an unreasonable amount
	                amountToTrim = Math.min(amountToTrim, item.getCurrentValue() * 0.20); // Cap at 20% of current value for trimming
	                if (amountToTrim > 0) {
	                    ideas.add(String.format("Your **%s** has performed exceptionally well (%.2f%% allocation). Consider **taking $%.2f in profits** to diversify and reduce concentration risk.",
	                            item.getName(), item.getAllocationPercentage(), amountToTrim));
	                    if (ideas.size() >= 3) break;
	                }
	            }
	        }

	        // --- Idea 3: Increase allocation for under-allocated but performing assets ---
	        for (PortfolioItem item : gainers) {
	            if (item.getAllocationPercentage() < UNDER_ALLOCATION_THRESHOLD_PERCENT && item.getProfitLoss() > 0) {
	                // Suggest an amount to bring it closer to the target minimum allocation
	                double targetValueForMinAllocation = totalCurrentValue * (TARGET_MIN_ALLOCATION_PERCENT / 100.0);
	                double suggestedInvestment = targetValueForMinAllocation - item.getCurrentValue();
	                if (suggestedInvestment > 0) {
	                    ideas.add(String.format("Your **%s** is performing well but is under-allocated (%.2f%%). Consider **investing an additional $%.2f** to boost its contribution to your portfolio's growth.",
	                            item.getName(), item.getAllocationPercentage(), suggestedInvestment));
	                    if (ideas.size() >= 3) break;
	                }
	            }
	        }

	        // --- Ensure at least three ideas are generated, filling with general advice if needed ---
	        while (ideas.size() < 3) {
	            if (ideas.size() == 0 && !losers.isEmpty()) {
	                // Fallback for a significant loss if previous specific suggestions didn't hit 3
	                PortfolioItem mostLostItem = losers.get(0);
	                double suggestedInvestment = Math.abs(mostLostItem.getProfitLoss()) * LOSS_COVERAGE_PERCENT * 0.75; // Slightly less than initial
	                ideas.add(String.format("Your portfolio has some areas to optimize. For instance, **%s** is currently in loss. A strategic investment of **$%.2f** could help improve its performance.",
	                        mostLostItem.getName(), suggestedInvestment));
	            } else if (ideas.size() == 0 && !gainers.isEmpty()) {
	                // Fallback for a good performing asset to increase allocation
	                PortfolioItem bestPerformingItem = gainers.get(0);
	                double suggestedInvestment = bestPerformingItem.getCurrentValue() * 0.05; // 5% of current value
	                ideas.add(String.format("Your **%s** asset is showing strong performance. Consider **investing an additional $%.2f** to capitalize further on its growth potential.",
	                        bestPerformingItem.getName(), suggestedInvestment));
	            }
	            else {
	                // Add general rebalancing advice if specific scenarios don't fill up slots
	                ideas.add("Regularly **review your asset allocation** to ensure it aligns with your financial goals and risk tolerance. Market fluctuations can shift your portfolio's balance over time.");
	                ideas.add("Consider **diversifying your investments** across different asset classes (e.g., real estate, bonds) to spread risk and potentially improve returns, especially if your current portfolio is concentrated.");
	                ideas.add("Don't forget to **reassess your risk profile** periodically. As your financial situation or goals change, your willingness or ability to take on risk might also change, necessitating portfolio adjustments.");
	            }

	            // Prevent infinite loop if no new unique ideas can be generated and size is still less than 3
	            if (ideas.size() == 0 && portfolioItems.isEmpty()) {
	                ideas.add("No portfolio data to analyze. Please add investment details to get personalized rebalancing ideas.");
	                break;
	            }
	             // Add a general healthy portfolio message if all else fails and portfolio is generally balanced
	            if (ideas.size() < 3 && portfolioItems.stream().allMatch(item -> item.getProfitLoss() >= 0 && item.getAllocationPercentage() >= UNDER_ALLOCATION_THRESHOLD_PERCENT && item.getAllocationPercentage() <= OVER_ALLOCATION_THRESHOLD_PERCENT)) {
	                ideas.add("Your portfolio appears well-balanced and healthy. Continue monitoring market conditions regularly and review your investment goals periodically.");
	            }
	            if (ideas.size() < 3 && !portfolioItems.isEmpty()) {
	                ideas.add("For a deeper dive, consider consulting a financial advisor to fine-tune your rebalancing strategy based on your unique financial situation and future aspirations.");
	            }
	        }
	        return ideas.stream().distinct().limit(3).collect(Collectors.toList()); // Ensure uniqueness and limit to 3
	    }
}
