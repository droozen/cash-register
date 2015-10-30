package com.roozen.register.model;

public class SalesTaxRate {

    private String description;
    private double rate;

    public SalesTaxRate(String description, double rate) {
        this.description = description;
        this.rate = rate;
    }

    public String getDescription() {
        return description;
    }

    public double getRate() {
        return rate;
    }

    @Override
    public String toString() {
        return "SalesTaxRate{" +
                "description='" + description + '\'' +
                ", rate=" + rate +
                '}';
    }
}
