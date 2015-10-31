package com.roozen.register.model;

import java.util.Date;

public class TenderRecord {

    private double amountTendered;
    private double changeGiven;
    private Date timestamp;

    public TenderRecord(double amountTendered, double grandTotal) {
        this.amountTendered = amountTendered;
        this.changeGiven = Math.round((amountTendered - grandTotal) * 100.0) / 100.0;
        this.timestamp = new Date();
    }

    public TenderRecord(double amountTendered, double changeGiven, Date timestamp) {
        this.amountTendered = amountTendered;
        this.changeGiven = changeGiven;
        this.timestamp = timestamp;
    }

    public double getAmountTendered() {
        return amountTendered;
    }

    public double getChangeGiven() {
        return changeGiven;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return "TenderRecord{" +
                "amountTendered=" + amountTendered +
                ", changeGiven=" + changeGiven +
                ", timestamp=" + timestamp +
                '}';
    }
}
