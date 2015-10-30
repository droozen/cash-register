package com.roozen.register.dao;

import com.roozen.register.model.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Transactional
public class ItemDao {

    @Autowired
    NamedParameterJdbcTemplate jdbcTemplate;

    private String findAllItemSql;
    private String findItemSql;

    public void setFindAllItemSql(String findAllItemSql) {
        this.findAllItemSql = findAllItemSql;
    }

    public void setFindItemSql(String findItemSql) {
        this.findItemSql = findItemSql;
    }

    public List<Item> findAllItems() {
        final List<Item> items = new ArrayList<>();

        jdbcTemplate.query(findAllItemSql, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet resultSet) throws SQLException {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                Double price = resultSet.getDouble("price");

                items.add(new Item(id, name, price));
            }
        });

        return items;
    }

    public Item findItem(int itemId) {
        final List<Item> items = new ArrayList<>();

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("id", itemId);

        jdbcTemplate.query(findItemSql, parameters, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet resultSet) throws SQLException {
                items.add(new Item(resultSet.getInt("id"), resultSet.getString("name"), resultSet.getDouble("price")));
            }
        });

        Assert.notEmpty(items);
        return items.get(0);
    }
}
