package com.cts.wealthmanagementsystem.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.cts.wealthmanagementsystem.entity.InvestmentPlan;
import com.cts.wealthmanagementsystem.entity.PortfolioItem;
import com.cts.wealthmanagementsystem.repository.InvestmentRepository;

public interface PortfolioService   {
	public List<PortfolioItem> getPortfolio(double goldBuyValue, double mutualFundsBuyValue, double shareMarketBuyValue,@ModelAttribute("portfolioItems") List<PortfolioItem> portfolioItems);
 
    public List<String> getRebalancingIdeas(List<PortfolioItem> portfolioItems);
	}