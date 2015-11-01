package com.roozen.register.model;

import java.text.DecimalFormat;
import java.util.Date;

public class OrderHeader {

    private int orderId;
    private Order.StatusCode statusCode;
    private Date timestamp;
    private Integer orderNumber;
    private double grandTotal;

    private DecimalFormat decimalFormat = new DecimalFormat("0.00");

    public OrderHeader(int orderId, String statusCode, Date timestamp, Integer orderNumber, double grandTotal) {
        this.orderId = orderId;
        this.statusCode = Order.StatusCode.valueOf(statusCode);
        this.timestamp = timestamp;
        this.orderNumber = orderNumber;
        this.grandTotal = grandTotal;
    }

    public int getOrderId() {
        return orderId;
    }

    public String getStatusCode() {
        return statusCode.name();
    }

    // TODO: We should consider returning a String that's been formatted to yyyy-MM-dd HH:mm:ss so the UI can know what to expect and translate according to how it needs to display
    public Date getTimestamp() {
        return timestamp;
    }

    public String getOrderNumber() {
        if (orderNumber == null) {
            return "";
        } else {
            return Integer.toString(orderNumber);
        }
    }

    public String getGrandTotal() {
        return decimalFormat.format(grandTotal);
    }
}
