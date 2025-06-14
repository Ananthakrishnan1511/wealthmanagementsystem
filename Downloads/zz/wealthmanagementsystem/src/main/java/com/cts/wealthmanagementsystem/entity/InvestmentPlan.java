package com.cts.wealthmanagementsystem.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class InvestmentPlan {  
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
	private Integer	planId ;
	private String	clientId ;
	private String	investmentObjective;
	private String	riskAppetite;
	private String allocationDetails;
	

}
