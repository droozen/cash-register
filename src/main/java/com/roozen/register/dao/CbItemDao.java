package com.roozen.register.dao;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.CouchbaseCluster;
import com.couchbase.client.java.query.N1qlQuery;
import com.couchbase.client.java.query.N1qlQueryResult;
import com.couchbase.client.java.query.N1qlQueryRow;
import com.couchbase.client.java.query.SimpleN1qlQuery;
import com.roozen.register.model.Item;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@ConfigurationProperties(prefix = "couchbase")
public class CbItemDao {

    private String host;
    private String bucketName;
    private String user;
    private String password;

    private String findAllItemSql;
    private String findItemSql;

    public void setFindAllItemSql(String findAllItemSql) {
        this.findAllItemSql = findAllItemSql;
    }

    public void setFindItemSql(String findItemSql) {
        this.findItemSql = findItemSql;
    }

    public List<Item> findAllItems() {
        Cluster cluster = connect();
        try {
            Bucket bucket = cluster.openBucket(bucketName);

            SimpleN1qlQuery query = N1qlQuery.simple(findAllItemSql.replace(":bucketName", bucketName));
            N1qlQueryResult result = bucket.query(query);

            final List<Item> items = new ArrayList<>();
            for (N1qlQueryRow row : result) {
                items.add(
                        new Item(
                                row.value().getObject("items").getInt("id"),
                                row.value().getObject("items").getString("name"),
                                row.value().getObject("items").getDouble("price"))
                );
            }
            return items;
        } finally {
            if (cluster != null) cluster.disconnect();
        }
    }

    public Item findItem(int itemId) {
        return null;
    }

    private Cluster connect() {
        return CouchbaseCluster.create(host);
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
