package com.roozen.register.dao;

import com.roozen.register.Server;
import com.roozen.register.model.Item;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * This is an integration test for the ItemDao that will actually execute database queries.
 * The ItemDao is a find only class at this time, so we don't have to worry about reverting changes.
 * However, these tests do expect some data to be populated in the item table.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Server.class)
@TestPropertySource(locations="classpath:test.properties")
public class ItemDaoTest {

    @Autowired
    ItemDao itemDao;

    @Autowired
    NamedParameterJdbcTemplate jdbcTemplate;

    private static Item testItem;
    private static final int testItemId = -1;

    @Test
    public void testFindAll() throws Exception {
        final Integer expectedCount = jdbcTemplate.queryForObject("select count(*) from item", new HashMap<>(), Integer.class);
        assertTrue(expectedCount > 0);

        // EXECUTE
        final List<Item> items = itemDao.findAllItems();

        // VERIFY
        assertEquals(expectedCount, (Integer)items.size());

        items.parallelStream().forEach(item -> {
            assertTrue(item.getPrice() >= 0.0);
            assertNotNull(item.getName());
            assertTrue(item.getName().length() > 0);
            assertTrue(item.getId() >= 0);
        });
    }

    @Test
    public void testFindById() throws Exception {
        try {
            // SETUP
            setUpTestItem();

            // EXECUTE
            Item actualItem = itemDao.findItem(testItem.getId());

            // VERIFY
            assertEquals(testItem, actualItem);
            assertEquals(testItem.getId(), actualItem.getId());
            assertEquals(testItem.getName(), actualItem.getName());
            assertEquals(testItem.getPrice(), actualItem.getPrice(), 0.01);
        } finally {
            tearDownTestItem();
        }
    }

    private void setUpTestItem() throws Exception {
        final double inputPrice = 4.99;

        Map<String,Object> parameters = new HashMap<>();
        parameters.put("id", testItemId);
        parameters.put("name", "TEST1");
        parameters.put("price", inputPrice);

        jdbcTemplate.update("insert into item (id, name, price) values (:id, :name, :price)", parameters);

        final List<Item> items = new ArrayList<>();
        jdbcTemplate.query("select id, name, price from item where id = :id", parameters, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet resultSet) throws SQLException {
                items.add(new Item(resultSet.getInt("id"), resultSet.getString("name"), resultSet.getDouble("price")));
            }
        });

        assertEquals(1, items.size());
        testItem = items.get(0);
    }

    private void tearDownTestItem() throws Exception {
        Map<String,Object> parameters = new HashMap<>();
        parameters.put("id", testItemId);

        jdbcTemplate.update("delete from item where id = :id", parameters);
    }

}
