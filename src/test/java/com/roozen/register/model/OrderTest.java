package com.roozen.register.model;

import org.junit.Test;

import java.util.Collection;
import java.util.Iterator;

import static org.junit.Assert.*;

public class OrderTest {

    @Test
    public void testAddRemoveItems() {
        final String pizzaName = "Pizza";
        final String sandwichName = "Sandwich";

        final Item pizza = new Item(0, pizzaName, 5.00);
        final Item sandwich = new Item(1, sandwichName, 4.95);

        Order order = new Order(0);
        assertTrue("We have line items before adding anything", order.getLineItems().isEmpty());

        // EXECUTE
        order.addLineItem(pizza);
        order.addLineItem(pizza);
        order.addLineItem(sandwich);

        // VERIFY
        {
            Collection<OrderLineItem> lineItems = order.getLineItems();
            Iterator<OrderLineItem> iterator = lineItems.iterator();
            OrderLineItem firstItem = iterator.next();

            assertEquals(pizzaName, firstItem.getName());
            assertEquals(2, firstItem.getQty());
            assertEquals(5.00, firstItem.getPrice(), 0.01); // TODO: Does price reflect the qty? or extended price?

            OrderLineItem secondItem = iterator.next();
            assertEquals(sandwichName, secondItem.getName());
            assertEquals(1, secondItem.getQty());
            assertEquals(4.95, secondItem.getPrice(), 0.01);

            assertEquals(14.95, order.getSubTotal(), 0.01);
        }

        // EXECUTE
        order.removeLineItem(sandwich);
        order.removeLineItem(sandwich);
        order.removeLineItem(pizza);

        // VERIFY
        {
            Collection<OrderLineItem> lineItems = order.getLineItems();
            Iterator<OrderLineItem> iterator = lineItems.iterator();
            OrderLineItem firstItem = iterator.next();

            assertEquals(pizzaName, firstItem.getName());
            assertEquals(1, firstItem.getQty());
            assertEquals(5.00, firstItem.getPrice(), 0.01);

            assertFalse("We should have removed all of the sandwiches", iterator.hasNext());

            assertEquals(5.00, order.getSubTotal(), 0.01);
        }
    }
}
