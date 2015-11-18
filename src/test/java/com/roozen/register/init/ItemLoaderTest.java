package com.roozen.register.init;

import com.roozen.register.Client;
import com.roozen.register.init.resource.Resource;
import com.roozen.register.init.resource.ResourceFactory;
import com.roozen.register.model.Item;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Client.class)
@TestPropertySource(locations="classpath:test.properties")
public class ItemLoaderTest {

    @Autowired
    ItemLoader itemLoader;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Before
    public void setUp() throws Exception {
        // Start fresh
        jdbcTemplate.execute("delete from item");
    }

    @After
    public void tearDown() throws Exception {
        // End clean
        jdbcTemplate.execute("delete from item");
    }

    @Test
    public void testLoadItems() throws Exception {
        final String[] inputCsv = {"Pizza, 5.00", "Sandwich, 4.95"};
        final String inputFileSource = "myTestFileSource";
        final boolean inputHeader = true;

        itemLoader.resourceFactory = new ResourceFactory() {
            @Override
            public Resource getFileResource(String fileSource) {
                assertEquals(inputFileSource, fileSource);
                return new MockFileResource(inputCsv, inputHeader);
            }
        };

        // EXECUTE
        itemLoader.loadItems(inputFileSource, inputHeader);

        // VERIFY
        List<Item> items = new ArrayList<>();
        jdbcTemplate.query("select id, name, price from item", new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet resultSet) throws SQLException {
                items.add(new Item(resultSet.getInt("id"), resultSet.getString("name"), resultSet.getDouble("price")));
            }
        });
        assertEquals(inputCsv.length, items.size());

        Iterator<Item> iterator = items.iterator();

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
