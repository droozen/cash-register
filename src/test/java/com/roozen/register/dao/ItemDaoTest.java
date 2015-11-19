package com.roozen.register.dao;

import com.roozen.register.Server;
import com.roozen.register.model.Item;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * This is an integration test for the ItemDao that will actually execute database queries.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Server.class)
@TestPropertySource(locations = "classpath:test.properties")
public class ItemDaoTest {

    @Autowired
    ItemDao itemDao;

    @Autowired
    NamedParameterJdbcTemplate jdbcTemplate;

    Object[][] itemData = {{0, "Pizza", 4.99}, {1, "Sandwich", 9.95}};

    @Before
    public void setUp() throws Exception {
        Map<String, Object>[] params = new HashMap[itemData.length];
        for (int i = 0; i < itemData.length; i++) {
            params[i] = new HashMap<>();
            params[i].put("id", itemData[i][0]);
            params[i].put("name", itemData[i][1]);
            params[i].put("price", itemData[i][2]);
        }

        int[] results = jdbcTemplate.batchUpdate("insert into item (id, name, price) values (:id, :name, :price)", params);
        assertEquals(2, results.length); // 2 inserts
        for (int result : results) assertEquals(1, result); // each insert updated 1 row
    }

    @After
    public void tearDown() throws Exception {
        jdbcTemplate.update("delete from item", new HashMap<>());
    }

    @Test
    public void testFindAll() throws Exception {
        final int expectedSize = 2;

        // EXECUTE
        final List<Item> items = itemDao.findAllItems();

        // VERIFY
        assertEquals((Integer) expectedSize, (Integer) items.size());

        for (int i = 0; i < expectedSize; i++) {
            Item item = items.get(i);
            assertEquals(itemData[i][0], item.getId());
            assertEquals(itemData[i][1], item.getName());
            assertEquals((Double) itemData[i][2], item.getPrice(), 0.01);
        }
    }

    @Test
    public void testFindById() throws Exception {
        final int inputId = 0;

        // EXECUTE
        Item actualItem = itemDao.findItem(inputId);

        // VERIFY
        assertEquals(itemData[inputId][0], actualItem.getId());
        assertEquals(itemData[inputId][1], actualItem.getName());
        assertEquals((Double) itemData[inputId][2], actualItem.getPrice(), 0.01);
    }

    @Test
    public void testFindById_doesNotExist() throws Exception {
        final int inputId = 2;

        // EXECUTE
        try {
            itemDao.findItem(inputId);
        } catch (Exception e) {
            return;
        }

        fail("We should have received an exception due to asking for an item we don't have.");
    }

}
