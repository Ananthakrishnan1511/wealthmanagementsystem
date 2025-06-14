package com.cts.wealthmanagementsystem.dao;

public class ChoosenPlan {
    private String planType;
    private String planName;
    private double entryPrice;

    public ChoosenPlan(String planType, String planName, double entryPrice) {
        this.planType = planType;
        this.planName = planName;
        this.entryPrice = entryPrice;
    }

    public String getPlanType() {
        return planType;
    }

    public String getPlanName() {
        return planName;
    }

    public double getEntryPrice() {
        return entryPrice;
    }
}
