package com.roozen.register.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class OrderLineItemTest {

    @Test
    public void testAddRemove() {
        Item item = new Item(0, "Pizza", 5.00);
        OrderLineItem lineItem = new OrderLineItem(item);

        assertEquals(0, lineItem.getQty());

        lineItem.add();
        assertEquals(1, lineItem.getQty());

        lineItem.add();
        lineItem.add();
        assertEquals(3, lineItem.getQty());

        lineItem.remove();
        assertEquals(2, lineItem.getQty());

        lineItem.remove();
        lineItem.remove();
        assertEquals(0, lineItem.getQty());

        lineItem.remove();
        assertEquals(0, lineItem.getQty());
    }
}
