package com.roozen.register.dao;

import com.roozen.register.model.Item;
import com.roozen.register.model.Order;
import com.roozen.register.model.OrderLineItem;
import com.roozen.register.model.TenderRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Component
@Transactional
public class OrderDao {

    @Autowired
    NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    ItemDao itemDao;

    private String createNewOrderSql;
    private String getMaxOrderIdSql;
    private String findOrderSql;
    private String findLineItemsSql;
    private String findTenderRecordsSql;

    private String insertLineItemSql;
    private String deleteLineItemsSql;

    public void setCreateNewOrderSql(String createNewOrderSql) {
        this.createNewOrderSql = createNewOrderSql;
    }

    public void setGetMaxOrderIdSql(String getMaxOrderIdSql) {
        this.getMaxOrderIdSql = getMaxOrderIdSql;
    }

    public void setFindOrderSql(String findOrderSql) {
        this.findOrderSql = findOrderSql;
    }

    public void setFindLineItemsSql(String findLineItemsSql) {
        this.findLineItemsSql = findLineItemsSql;
    }

    public void setFindTenderRecordsSql(String findTenderRecordsSql) {
        this.findTenderRecordsSql = findTenderRecordsSql;
    }

    public void setInsertLineItemSql(String insertLineItemSql) {
        this.insertLineItemSql = insertLineItemSql;
    }

    public void setDeleteLineItemsSql(String deleteLineItemsSql) {
        this.deleteLineItemsSql = deleteLineItemsSql;
    }

    public synchronized Order createNewOrder() {
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

    // TODO: Test and confirm that @Transactional is good enough to make this happen in a single transaction.
    public synchronized Order addItem(Integer orderId, Integer itemId) {
        Order order = findOrder(orderId);
        Item item = itemDao.findItem(itemId);

        order.addLineItem(item);
        updateOrder(order);

        return order;
    }

    private void updateOrder(Order order) {
        deleteLineItems(order);
        insertLineItems(order);
    }

    private void deleteLineItems(Order order) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("id", order.getOrderId());

        jdbcTemplate.update(deleteLineItemsSql, parameters);
    }

    private void insertLineItems(Order order) {
        Map<String, Object>[] batchParams = new HashMap[order.getLineItems().size()];

        int index = 0;
        for (OrderLineItem item : order.getLineItems()) {
            batchParams[index] = getParams(order, item);
            index++;
        }

        jdbcTemplate.batchUpdate(insertLineItemSql, batchParams);
    }

    private Map<String, Object> getParams(Order order, OrderLineItem item) {
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("id", order.getOrderId());
        parameters.put("item_id", item.getType().getId());
        parameters.put("name", item.getName());
        parameters.put("qty", item.getQty());
        parameters.put("price", item.getPrice());
        return parameters;
    }

    /**
     * Fill entire order model
     *
     * @param orderId
     * @return
     */
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

        Assert.isTrue(orders.size() > 0);
        final Order order = orders.get(0);

        final List<OrderLineItem> lineItems = new ArrayList<>();
        jdbcTemplate.query(findLineItemsSql, parameters, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet resultSet) throws SQLException {
                int itemId = resultSet.getInt("item_id");
                String itemName = resultSet.getString("name");
                double price = resultSet.getDouble("price");
                int qty = resultSet.getInt("qty");

                // TODO: Do we care to keep the item price sanitized in the Item object in memory here?
                // (e.g. the item price could have changed since we last looked at this order)
                // But we are using the correct order_line_item.price
                Item item = new Item(itemId, itemName, price);
                lineItems.add(new OrderLineItem(item, qty, price));
            }
        });
        order.addAllLineItems(lineItems);

        final List<TenderRecord> tenders = new ArrayList<>();
        jdbcTemplate.query(findTenderRecordsSql, parameters, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet resultSet) throws SQLException {
                double amount = resultSet.getDouble("amount");
                double changeAmount = resultSet.getDouble("change_amt");
                Date timestamp = resultSet.getDate("timestamp");

                tenders.add(new TenderRecord(amount, changeAmount, timestamp));
            }
        });

        if (tenders.size() > 0) {
            order.setTenderRecord(tenders.get(0));
        }

        return order;
    }
}
