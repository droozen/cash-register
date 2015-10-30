package com.roozen.register.dao;

import com.roozen.register.model.Item;
import com.roozen.register.model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Component
public class OrderDao {

    @Autowired
    NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    ItemDao itemDao;

    private String createNewOrderSql;
    private String getMaxOrderIdSql;
    private String findOrderSql;

    private String insertLineItemSql;

    public void setCreateNewOrderSql(String createNewOrderSql) {
        this.createNewOrderSql = createNewOrderSql;
    }

    public void setGetMaxOrderIdSql(String getMaxOrderIdSql) {
        this.getMaxOrderIdSql = getMaxOrderIdSql;
    }

    public void setFindOrderSql(String findOrderSql) {
        this.findOrderSql = findOrderSql;
    }

    public void setInsertLineItemSql(String insertLineItemSql) {
        this.insertLineItemSql = insertLineItemSql;
    }

    @Transactional
    public Order createNewOrder() {
        int maxId = getMaxOrderId();

        Order order = new Order(maxId + 1);

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("id", order.getOrderId());
        parameters.put("subtotal", order.getSubTotal());
        parameters.put("tax", order.getTotalTax());
        parameters.put("grandtotal", order.getGrandTotal());

        jdbcTemplate.update(createNewOrderSql, parameters);

        return order;
    }

    public int getMaxOrderId() {
        return jdbcTemplate.queryForObject(getMaxOrderIdSql, new HashMap<>(), Integer.class);
    }

    @Transactional
    public void addItem(Integer orderId, String itemName) {
        Order order = findOrder(orderId);
        Item item = itemDao.findItem(itemName);


    }

    public Order findOrder(Integer orderId) {
        final List<Order> orders = new ArrayList<Order>();

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("id", orderId);

        jdbcTemplate.query(findOrderSql, parameters, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet resultSet) throws SQLException {
                int id = resultSet.getInt("id");
                int orderNumber = resultSet.getInt("orderno");
                Date timestamp = resultSet.getDate("timestamp");

                orders.add(new Order(id, orderNumber, timestamp));
            }
        });
    }
}
