package com.roozen.register.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class OrderLineItemTest {

    @Test
    public void testAddRemove() {
        final double price = 5.00;
        final Item item = new Item(0, "Pizza", price);
        final OrderLineItem lineItem = new OrderLineItem(item);

        assertLineItem(lineItem, 0, price, 0.00);

        lineItem.add();
        assertLineItem(lineItem, 1, price, 5.00);

        lineItem.add();
        lineItem.add();
        assertLineItem(lineItem, 3, price, 15.00);

        lineItem.remove();
        assertLineItem(lineItem, 2, price, 10.00);

        lineItem.remove();
        lineItem.remove();
        assertLineItem(lineItem, 0, price, 0.00);

        lineItem.remove();
        assertLineItem(lineItem, 0, price, 0.00);
    }

    @Test
    public void testSetQty() throws Exception {
        final double price = 4.95;
        final Item item = new Item(0, "Pizza", 4.95);
        OrderLineItem lineItem = new OrderLineItem(item);
        assertEquals(4.95, lineItem.getPrice(), 0.01);

        lineItem.setQty(3);
        assertLineItem(lineItem, 3, price, 14.85);

        lineItem.setQty(30);
        assertLineItem(lineItem, 30, price, 148.50);

        lineItem.setQty(0);
        assertLineItem(lineItem, 0, price, 0.00);

        lineItem.setQty(-5);
        assertLineItem(lineItem, 0, price, 0.00);
    }

    private void assertLineItem(OrderLineItem lineItem, int qty, double price, double extendedPrice) {
        assertEquals("Qty mismatch", qty, lineItem.getQty());
        assertEquals("Price mismatch", price, lineItem.getPrice(), 0.01);
        assertEquals("Extended price mismatch", extendedPrice, lineItem.getExtendedPrice(), 0.01);
    }
}
