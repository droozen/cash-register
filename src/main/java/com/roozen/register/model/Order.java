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

    public Order(int orderId) {
        this.orderId = orderId;
        this.lineItems = new TreeMap<>();

        this.subTotal = 0.00;
        this.totalTax = 0.00;
        this.grandTotal = 0.00;
    }

    public void addLineItem(Item item) {
        if (lineItems.containsKey(item) == false) {
            lineItems.put(item, new OrderLineItem(item));
        }
        lineItems.get(item).add();
    }

    public void removeLineItem(Item item) {
        if (lineItems.containsKey(item) == false) return;

        OrderLineItem orderLineItem = lineItems.get(item);
        orderLineItem.remove();
        if (orderLineItem.getQty() <= 0) {
            lineItems.remove(item);
        }
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

    public Collection<OrderLineItem> getLineItems() {
        return lineItems.values();
    }

    public TenderRecord getTenderRecord() {
        return tenderRecord;
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
