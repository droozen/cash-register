package com.roozen.register.model.view;

import com.roozen.register.model.OrderLineItem;

import java.text.DecimalFormat;

public class LineItemView {

    private OrderLineItem lineItem;

    private DecimalFormat decimalFormat = new DecimalFormat("0.00");

    public LineItemView(OrderLineItem lineItem) {
        this.lineItem = lineItem;
    }

    public int getQty() {
        return this.lineItem.getQty();
    }

    public String getPrice() {
        return "$" + decimalFormat.format(this.lineItem.getPrice());
    }

    public String getExtendedPrice() {
        return "$" + decimalFormat.format(this.lineItem.getExtendedPrice());
    }

    public String getName() {
        return this.lineItem.getName();
    }

    public ItemView getType() {
        return new ItemView(this.lineItem.getType());
    }
}
