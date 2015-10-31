package com.roozen.register.model.view;

import com.roozen.register.model.Order;
import com.roozen.register.model.OrderLineItem;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

public class OrderView {

    private Order order;
    private Collection<LineItemView> lineItems;
    private TenderRecordView tenderRecord;

    private DecimalFormat decimalFormat = new DecimalFormat("0.00");

    public OrderView(Order order) {
        this.order = order;
        this.lineItems = new ArrayList<>();

        if (this.order.getLineItems() != null) {
            for (OrderLineItem lineItem : this.order.getLineItems()) {
                lineItems.add(new LineItemView(lineItem));
            }
        }

        if (this.order.getTenderRecord() != null) {
            this.tenderRecord = new TenderRecordView(this.order.getTenderRecord());
        }
    }

    public int getOrderId() {
        return this.order.getOrderId();
    }

    public Integer getOrderNumber() {
        return this.order.getOrderNumber();
    }

    public Date getTimestamp() {
        return this.order.getTimestamp();
    }

    public String getSubTotal() {
        return "$" + decimalFormat.format(this.order.getSubTotal());
    }

    public String getTotalTax() {
        return "$" + decimalFormat.format(this.order.getTotalTax());
    }

    public String getGrandTotal() {
        return "$" + decimalFormat.format(this.order.getSubTotal());
    }

    public Collection<LineItemView> getLineItems() {
        return lineItems;
    }

    public TenderRecordView getTenderRecord() {
        return tenderRecord;
    }
}
