package com.roozen.register.dao;

import com.roozen.register.model.*;
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

    @Autowired
    TaxDao taxDao;

    private String createNewOrderSql;
    private String getMaxOrderIdSql;
    private String findOrderSql;
    private String findLineItemsSql;
    private String findTenderRecordsSql;
    private String findOrderHeadersSql;
    private String findMaxOrderNumberSql;

    private String insertLineItemSql;
    private String deleteLineItemsSql;
    private String updateOrderSql;
    private String insertTenderRecordSql;

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

    public void setFindOrderHeadersSql(String findOrderHeadersSql) {
        this.findOrderHeadersSql = findOrderHeadersSql;
    }

    public void setFindMaxOrderNumberSql(String findMaxOrderNumberSql) {
        this.findMaxOrderNumberSql = findMaxOrderNumberSql;
    }

    public void setInsertLineItemSql(String insertLineItemSql) {
        this.insertLineItemSql = insertLineItemSql;
    }

    public void setDeleteLineItemsSql(String deleteLineItemsSql) {
        this.deleteLineItemsSql = deleteLineItemsSql;
    }

    public void setUpdateOrderSql(String updateOrderSql) {
        this.updateOrderSql = updateOrderSql;
    }

    public void setInsertTenderRecordSql(String insertTenderRecordSql) {
        this.insertTenderRecordSql = insertTenderRecordSql;
    }

    public synchronized Order createNewOrder() {
        int maxId = getMaxOrderId();

        Order order = new Order(maxId + 1);
        order.setTaxRate(taxDao.findSalesTaxRate());

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("id", order.getOrderId());
        parameters.put("subtotal", order.getSubTotal());
        parameters.put("tax", order.getTotalTax());
        parameters.put("grandtotal", order.getGrandTotal());
        parameters.put("statusCode", order.getStatusCode().name());

        jdbcTemplate.update(createNewOrderSql, parameters);

        return order;
    }

    public int getMaxOrderId() {
        return jdbcTemplate.queryForObject(getMaxOrderIdSql, new HashMap<>(), Integer.class);
    }

    // TODO: Test and confirm that @Transactional is good enough to make this happen in a single transaction.
    public Order addItem(Integer orderId, Integer itemId) {
        Assert.notNull(orderId);
        Assert.notNull(itemId);

        Order order = findOrder(orderId);
        Item item = itemDao.findItem(itemId);

        order.addLineItem(item);
        updateOrder(order);

        return order;
    }

    public void updateOrder(Order order) {
        Assert.notNull(order);

        deleteLineItems(order);
        insertLineItems(order);

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("id", order.getOrderId());
        parameters.put("orderno", order.getOrderNumber());
        parameters.put("subtotal", order.getSubTotal());
        parameters.put("tax", order.getTotalTax());
        parameters.put("grandtotal", order.getGrandTotal());
        parameters.put("statusCode", order.getStatusCode().name());

        jdbcTemplate.update(updateOrderSql, parameters);

        insertTenderRecord(order);
    }

    private void insertTenderRecord(Order order) {
        Assert.notNull(order);
        if (order.getTenderRecord() == null) return;

        final List<Boolean> recordExists = new ArrayList<>();
        jdbcTemplate.query("select * from tender_record where order_id = " + order.getOrderId(), new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet resultSet) throws SQLException {
                recordExists.add(true);
            }
        });
        if (recordExists.isEmpty() == false) return;

        final TenderRecord record = order.getTenderRecord();

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("orderId", order.getOrderId());
        parameters.put("amount", record.getAmountTendered());
        parameters.put("changeAmt", record.getChangeGiven());

        jdbcTemplate.update(insertTenderRecordSql, parameters);

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
        parameters.put("extendedPrice", item.getExtendedPrice());
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
                Integer orderNumber = resultSet.getInt("orderno");
                orderNumber = (resultSet.wasNull() ? null : orderNumber);
                Date timestamp = resultSet.getDate("timestamp");
                String statusCode = resultSet.getString("status_cd");

                orders.add(new Order(id, orderNumber, statusCode, timestamp));
            }
        });

        Assert.isTrue(orders.size() > 0);
        final Order order = orders.get(0);
        order.setTaxRate(taxDao.findSalesTaxRate());

        final List<OrderLineItem> lineItems = new ArrayList<>();
        jdbcTemplate.query(findLineItemsSql, parameters, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet resultSet) throws SQLException {
                int itemId = resultSet.getInt("item_id");
                String itemName = resultSet.getString("name");
                double price = resultSet.getDouble("price");
                int qty = resultSet.getInt("qty");
                double extendedPrice = resultSet.getDouble("extended_price");

                // TODO: Do we care to keep the item price sanitized in the Item object in memory here?
                // (e.g. the item price could have changed since we last looked at this order)
                // But we are using the correct order_line_item.price
                Item item = new Item(itemId, itemName, price);
                lineItems.add(new OrderLineItem(item, qty, price, extendedPrice));
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

    public List<OrderHeader> findAllOrders() {
        final List<OrderHeader> orderHeaders = new ArrayList<>();
        jdbcTemplate.query(findOrderHeadersSql, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet resultSet) throws SQLException {
                int orderId = resultSet.getInt("id");
                Integer orderNumber = resultSet.getInt("orderno");
                if (resultSet.wasNull()) orderNumber = null;
                double grandTotal = resultSet.getDouble("grandtotal");
                String statusCode = resultSet.getString("status_cd");
                Date timestamp = resultSet.getDate("timestamp");

                orderHeaders.add(new OrderHeader(orderId, statusCode, timestamp, orderNumber, grandTotal));
            }
        });
        return orderHeaders;
    }

    public Order removeItem(Integer orderId, Integer itemId) {
        Order order = findOrder(orderId);
        Item item = itemDao.findItem(itemId);
        order.removeLineItem(item);

        updateOrder(order);
        return order;
    }

    public Order changeQty(Integer orderId, Integer itemId, Integer qty) {
        Order order = findOrder(orderId);
        Item item = itemDao.findItem(itemId);
        order.changeQty(item, qty);

        updateOrder(order);
        return order;
    }

    public synchronized Order completeOrder(Integer orderId, Double tender) {
        Order order = findOrder(orderId);
        order.setTenderRecord(new TenderRecord(tender, order.getGrandTotal()));
        order.setStatusCode(Order.StatusCode.PAID);

        int maxOrderNumber = findMaxOrderNumber();
        int orderNumber = (maxOrderNumber + 1) % 100;
        if (orderNumber == 0) orderNumber = 1;
        order.setOrderNumber(orderNumber);

        updateOrder(order);
        return order;
    }

    public int findMaxOrderNumber() {
        return jdbcTemplate.queryForObject(findMaxOrderNumberSql, new HashMap<>(), Integer.class);
    }

}
