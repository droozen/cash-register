package com.roozen.register.service;

import com.roozen.register.model.Item;
import com.roozen.register.service.resource.Resource;
import com.roozen.register.service.resource.ResourceFactory;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Iterator;

import static org.junit.Assert.*;

public class ItemLoaderTest {

    @Test
    public void testLoadItems() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        final String[] inputCsv = {"Pizza,5.00", "Sandwich,4.95"};
        final String inputFileSource = "myTestFileSource";
        final boolean inputHeader = true;

        ItemLoader loader = new ItemLoader();
        loader.resourceFactory = new ResourceFactory() {
            @Override
            public Resource getFileResource(String fileSource) {
                assertEquals(inputFileSource, fileSource);
                return new MockFileResource(inputCsv, inputHeader);
            }
        };

        // EXECUTE
        Method privateMethod = ItemLoader.class.getDeclaredMethod("parseItemsFromFile", String.class, boolean.class);
        privateMethod.setAccessible(true);
        Object returnValue = privateMethod.invoke(loader, inputFileSource, inputHeader);
        privateMethod.setAccessible(false);

        // VERIFY
        assertTrue(returnValue instanceof Collection);

        Collection<Item> itemsList = (Collection<Item>) returnValue;
        Iterator<Item> iterator = itemsList.iterator();
        assertTrue("Missing first item", iterator.hasNext());
        {
            Item item = iterator.next();
            assertEquals("Pizza", item.getName());
            assertEquals(5.00, item.getPrice(), 0.01);
        }

        assertTrue("Missing second item", iterator.hasNext());
        {
            Item item = iterator.next();
            assertEquals("Sandwich", item.getName());
            assertEquals(4.95, item.getPrice(), 0.01);
        }

        assertFalse("We should have only loaded two items", iterator.hasNext());
    }

    class MockFileResource implements Resource {

        private String[] csv;
        private int counter = 0;

        public MockFileResource(String[] csv, boolean header) {
            if (header) counter = -1;
            this.csv = csv;
        }

        @Override
        public String readBlock() throws Exception {
            if (counter == -1) {
                counter = 0;
                return null; // throw away header line
            }

            String value = null;
            if (csv.length > counter) {
                value = csv[counter];
                counter++;
            }
            return value;
        }

        @Override
        public void close() throws Exception {
            counter = 0;
        }

        public int getCounter() {
            return counter;
        }
    }
}
