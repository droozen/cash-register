package com.roozen.register.model.view;

import com.roozen.register.model.Item;

import java.text.DecimalFormat;

public class ItemView {

    private int id;
    private String name;
    private String price;

    private DecimalFormat decimalFormat = new DecimalFormat("0.00");

    public ItemView(Item item) {
        this.id = item.getId();
        this.name = item.getName();
        this.price = decimalFormat.format(item.getPrice());
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return "$" + price;
    }
}
