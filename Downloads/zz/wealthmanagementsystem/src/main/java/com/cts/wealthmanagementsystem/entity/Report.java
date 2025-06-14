package com.cts.wealthmanagementsystem.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long reportId;

    private LocalDate reportDate;

    private double totalCurrentValue;
    private double totalProfitLoss;

    @OneToMany(cascade = CascadeType.MERGE)
    private List<PortfolioItem> portfolioItems;
    
    public Report(LocalDate reportDate, double totalCurrentValue,double totalProfitLoss,List<PortfolioItem> portfolioItems) {
    	this.reportDate=reportDate;
    	this.totalCurrentValue=totalCurrentValue;
    	this.totalProfitLoss=totalProfitLoss;
    	this.portfolioItems=portfolioItems;
    }

    
}
