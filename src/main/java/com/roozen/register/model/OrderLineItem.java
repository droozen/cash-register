package com.roozen.register.model;

public class OrderLineItem {

    private int qty;
    private double price;
    private double extendedPrice;

    private Item type;

    public OrderLineItem(Item type) {
        if (type == null) {
            throw new NullPointerException("Cannot instantiate a line item with null object");
        }

        this.qty = 0;

        this.type = type;
        this.price = type.getPrice();
        this.extendedPrice = type.getPrice();
    }

    public void add() {
        this.qty = this.qty + 1;
    }

    public void remove() {
        if (this.qty > 0) {
            this.qty = this.qty - 1;
        }
    }

    public int getQty() {
        return qty;
    }

    public double getPrice() {
        return price;
    }

    public double getExtendedPrice() {
        return extendedPrice;
    }

    public String getName() {
        return type.getName();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OrderLineItem)) return false;

        OrderLineItem that = (OrderLineItem) o;

        return !(type != null ? !type.equals(that.type) : that.type != null);

    }

    @Override
    public int hashCode() {
        return type != null ? type.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "OrderLineItem{" +
                "qty=" + qty +
                ", price=" + price +
                ", extendedPrice=" + extendedPrice +
                ", type=" + type +
                '}';
    }
}
