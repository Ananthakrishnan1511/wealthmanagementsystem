package com.cts.wealthmanagementsystem.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Data 
@NoArgsConstructor 
@AllArgsConstructor
public class PortfolioItem {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer portItem;
    private Integer clientId;
    private String name;               
    private double buyValue;            
    private double currentValue;        
    private double profitLoss;         
    private double allocationPercentage; 
    
	public PortfolioItem(String name, double buyValue, double currentValue, double profitLoss,
			double allocationPercentage) {
		super();
		this.name = name;
		this.buyValue = buyValue;
		this.currentValue = currentValue;
		this.profitLoss = profitLoss;
		this.allocationPercentage = allocationPercentage;
	}
    
}