package com.cts.wealthmanagementsystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;


import com.cts.wealthmanagementsystem.entity.InvestmentPlan;

public interface InvestmentRepository extends JpaRepository<InvestmentPlan, Integer>{

}
