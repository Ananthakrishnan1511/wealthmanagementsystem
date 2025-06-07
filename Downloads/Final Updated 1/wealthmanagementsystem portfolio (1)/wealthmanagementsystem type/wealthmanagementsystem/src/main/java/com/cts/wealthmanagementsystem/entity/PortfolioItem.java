package com.cts.wealthmanagementsystem.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a single investment item in the portfolio.
 * Uses Lombok annotations for automatic generation of getters, setters,
 * constructors, equals, hashCode, and toString methods.
 */
@Entity
@Data // Generates getters, setters, toString, equals, and hashCode
@NoArgsConstructor // Generates a no-argument constructor
@AllArgsConstructor // Generates a constructor with all fields


public class PortfolioItem {
	
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer portItem;
    private Integer clientId;
    private String name;                // Name of the asset (e.g., "Gold", "Mutual Funds")
    private double buyValue;            // The initial value at which the asset was bought
    private double currentValue;        // The current market value of the asset
    private double profitLoss;          // Calculated profit or loss (currentValue - buyValue)
    private double allocationPercentage; // Percentage of this asset in the total current portfolio value
    
   

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