package com.roozen.register.model;

import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

public class Order {

    private int orderId;
    private Integer orderNumber;
    private Date timestamp;
    private double subTotal;
    private double totalTax;
    private double grandTotal;

    private Map<Item, OrderLineItem> lineItems;
    private TenderRecord tenderRecord;
    private Double taxRate;

    public Order(int orderId) {
        this.orderId = orderId;
        this.timestamp = new Date();
        this.lineItems = new TreeMap<>();

        this.subTotal = 0.00;
        this.totalTax = 0.00;
        this.grandTotal = 0.00;
    }

    public Order(int orderId, Integer orderNumber, Date timestamp) {
        this.orderId = orderId;
        this.orderNumber = orderNumber;
        this.timestamp = timestamp;
        this.lineItems = new TreeMap<>();

        this.subTotal = 0.00;
        this.totalTax = 0.00;
        this.grandTotal = 0.00;
    }

    public void addAllLineItems(Collection<OrderLineItem> items) {
        for (OrderLineItem lineItem : items) {
            lineItems.put(lineItem.getType(), lineItem);
        }
        calculateTotals();
    }

    public void addLineItem(Item item) {
        if (lineItems.containsKey(item) == false) {
            lineItems.put(item, new OrderLineItem(item));
        }
        lineItems.get(item).add();
        calculateTotals();
    }

    public void removeLineItem(Item item) {
        if (lineItems.containsKey(item) == false) return;

        OrderLineItem orderLineItem = lineItems.get(item);
        orderLineItem.remove();
        if (orderLineItem.getQty() <= 0) {
            lineItems.remove(item);
        }
        calculateTotals();
    }

    public void changeQty(Item item, Integer qty) {
        if (lineItems.containsKey(item) == false) {
            lineItems.put(item, new OrderLineItem(item));
        }
        lineItems.get(item).setQty(qty);
        calculateTotals();
    }

    private void calculateTotals() {
        calculateSubTotal();
        calculateTax();
        calculateGrandTotal();
    }

    private void calculateSubTotal() {
        double sum = 0;
        for (OrderLineItem lineItem : lineItems.values()) {
            sum += lineItem.getExtendedPrice();
        }
        subTotal = roundToMoney(sum);
    }

    private void calculateTax() {
        if (taxRate == null) return;
        totalTax = roundToMoney(subTotal * taxRate);
    }

    private void calculateGrandTotal() {
        grandTotal = roundToMoney(subTotal + totalTax);
    }

    private double roundToMoney(double value) {
        return Math.round(value * 100.0) / 100.0;
    }

    public int getOrderId() {
        return orderId;
    }

    public Integer getOrderNumber() {
        return orderNumber;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public double getSubTotal() {
        return subTotal;
    }

    public double getTotalTax() {
        return totalTax;
    }

    public double getGrandTotal() {
        return grandTotal;
    }

    // TODO: Consider letting the Order object fetch the tax rate from a third party to avoid mistakes missing setting it on this object.
    public void setTaxRate(Double taxRate) {
        this.taxRate = taxRate;
    }

    public Collection<OrderLineItem> getLineItems() {
        return lineItems.values();
    }

    public TenderRecord getTenderRecord() {
        return tenderRecord;
    }

    public void setTenderRecord(TenderRecord tenderRecord) {
        this.tenderRecord = tenderRecord;
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderNumber=" + orderNumber +
                ", timestamp=" + timestamp +
                ", subTotal=" + subTotal +
                ", totalTax=" + totalTax +
                ", grandTotal=" + grandTotal +
                ", lineItems=" + lineItems +
                ", tenderRecord=" + tenderRecord +
                '}';
    }
}
