package com.roozen.register.dao;

import com.roozen.register.model.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class ItemDao {

    @Autowired
    JdbcTemplate jdbcTemplate;

    private String findAllItemSql;

    public void setFindAllItemSql(String findAllItemSql) {
        this.findAllItemSql = findAllItemSql;
    }

    public List<Item> findAllItems() {
        final List<Item> items = new ArrayList<>();

        jdbcTemplate.query(findAllItemSql, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet resultSet) throws SQLException {
                String name = resultSet.getString("name");
                Double price = resultSet.getDouble("price");

                items.add(new Item(name, price));
            }
        });

        return items;
    }
}
