package com.cts.wealthmanagementsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cts.wealthmanagementsystem.entity.InvestmentPlan;
import com.cts.wealthmanagementsystem.entity.PortfolioItem;

public interface PortfolioRepository extends JpaRepository<PortfolioItem, Integer>{

}
