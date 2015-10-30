package com.roozen.register.model;

public class Item implements Comparable {

    private int id;
    private String name;

    // TODO: Consider using BigDecimal or a custom object for all money fields to perform proper money calculations
    // Though that may not be needed for this simple demonstration.
    private double price;

    public Item(int id, String name, double price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public int getId() {
        return id;
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

        return id == item.id;

    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", name='" + name + '\'' +
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
