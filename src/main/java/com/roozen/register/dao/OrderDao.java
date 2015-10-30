package com.roozen.register.dao;

import com.roozen.register.model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Component
public class OrderDao {

    @Autowired
    NamedParameterJdbcTemplate jdbcTemplate;

    private String createNewOrderSql;
    private String getMaxOrderIdSql;

    public void setCreateNewOrderSql(String createNewOrderSql) {
        this.createNewOrderSql = createNewOrderSql;
    }

    public void setGetMaxOrderIdSql(String getMaxOrderIdSql) {
        this.getMaxOrderIdSql = getMaxOrderIdSql;
    }

    @Transactional
    public Order createNewOrder() {
        int nextId = getNextOrderId();

        Order order = new Order(nextId);

        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("id", order.getOrderId());
        parameters.put("subtotal", order.getSubTotal());
        parameters.put("tax", order.getTotalTax());
        parameters.put("grandtotal", order.getGrandTotal());

        jdbcTemplate.update(createNewOrderSql, parameters);

        return new Order(nextId);
    }

    public int getNextOrderId() {
        return jdbcTemplate.queryForObject(getMaxOrderIdSql, new HashMap<String, Object>(), Integer.class);
    }
}
