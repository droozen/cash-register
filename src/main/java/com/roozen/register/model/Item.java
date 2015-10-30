package com.roozen.register.model;

public class Item implements Comparable {

    private String name;

    // TODO: Consider using BigDecimal or a custom object for all money fields to perform proper money calculations
    // Though that may not be needed for this simple demonstration.
    private double price;

    public Item(String name, double price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Item)) return false;

        Item item = (Item) o;

        if (Double.compare(item.price, price) != 0) return false;
        return !(name != null ? !name.equals(item.name) : item.name != null);
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = name != null ? name.hashCode() : 0;
        temp = Double.doubleToLongBits(price);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "Item{" +
                "name='" + name + '\'' +
                ", price=" + price +
                '}';
    }

    @Override
    public int compareTo(Object o) {
        if (o == null || !(o instanceof Item)) return 1;
        if (this.equals(o)) return 0;

        return this.getName().compareTo(((Item) o).getName());
    }
}
