package com.roozen.register.dao;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.CouchbaseCluster;
import com.couchbase.client.java.document.JsonDocument;
import com.couchbase.client.java.document.json.JsonObject;
import com.roozen.register.Server;
import com.roozen.register.model.Item;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * This is an integration test for the ItemDao that will actually execute database queries.
 * The ItemDao is a find only class at this time, so we don't have to worry about reverting changes.
 * However, these tests do expect some data to be populated in the item table.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Server.class)
@TestPropertySource(locations = "classpath:test.properties")
@ConfigurationProperties(prefix = "couchbase")
public class ItemDaoTest {

    @Autowired
    CbItemDao cbItemDao;

    @Autowired
    NamedParameterJdbcTemplate jdbcTemplate;

    private static final int testItemId = 0;
    private static final String testItemName = "TEST1";
    private static final double testItemPrice = 4.99;

    private String host;
    private String bucketName;
    private String user;
    private String password;

    @Before
    public void setUp() throws Exception {
        JsonObject item = JsonObject.empty()
                .put("id", testItemId)
                .put("name", testItemName)
                .put("price", testItemPrice)
                .put("type", "item");

        Cluster cluster = connect();
        Bucket bucket = bucket(cluster);
        bucket.upsert(JsonDocument.create("item::" + testItemId, item));
        cluster.disconnect();
    }

    @After
    public void tearDown() throws Exception {
        Cluster cluster = connect();
        Bucket bucket = bucket(cluster);

        String documentId = "item::" + testItemId;
        if (bucket.exists(documentId)) {
            bucket.remove(documentId);
        }
        cluster.disconnect();
    }

    @Test
    public void testFindAll() throws Exception {
        // EXECUTE
        final List<Item> items = cbItemDao.findAllItems();

        // VERIFY
        assertEquals((Integer) 1, (Integer) items.size());

        Item item = items.get(0);
        assertEquals(testItemId, item.getId());
        assertEquals(testItemName, item.getName());
        assertEquals(testItemPrice, item.getPrice(), 0.01);
    }

    @Test
    public void testFindById() throws Exception {
//        // EXECUTE
//        Item actualItem = cbItemDao.findItem(testItem.getId());
//
//        // VERIFY
//        assertEquals(testItem, actualItem);
//        assertEquals(testItem.getId(), actualItem.getId());
//        assertEquals(testItem.getName(), actualItem.getName());
//        assertEquals(testItem.getPrice(), actualItem.getPrice(), 0.01);
    }

    private Cluster connect() {
        return CouchbaseCluster.create(host);
    }

    private Bucket bucket(Cluster cluster) {
        return cluster.openBucket(bucketName);
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
