package com.roozen.register.controller;

import com.roozen.register.dao.ItemDao;
import com.roozen.register.dao.OrderDao;
import com.roozen.register.model.Order;
import com.roozen.register.model.TenderRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/controller/order")
public class OrderController {

    @Autowired
    OrderDao orderDao;

    @Autowired
    ItemDao itemDao;

    @RequestMapping("/create")
    @ResponseBody
    public Order newOrder() {
        return orderDao.createNewOrder();
    }

    @RequestMapping("/item/add")
    @ResponseBody
    public Order addItem(@RequestParam(value = "order") Integer orderId,
                         @RequestParam(value = "item") Integer itemId) {
        Assert.notNull(orderId);
        Assert.notNull(itemId);

        return orderDao.addItem(orderId, itemId);
    }

    @RequestMapping("/item/remove")
    @ResponseBody
    public Order removeItem(@RequestParam(value = "order") Integer orderId,
                            @RequestParam(value = "item") Integer itemId) {
        Assert.notNull(orderId);
        Assert.notNull(itemId);

        return orderDao.removeItem(orderId, itemId);
    }

    @RequestMapping("/find")
    @ResponseBody
    public Order findOrder(@RequestParam(value = "order") Integer orderId) {
        Assert.notNull(orderId);

        return orderDao.findOrder(orderId);
    }

    // TODO: No requirement to be able to change qty in this manner. Is this API needed?
    @RequestMapping("/item/change")
    @ResponseBody
    public Order changeItem(@RequestParam(value = "order") Integer orderId,
                            @RequestParam(value = "item") Integer itemId,
                            @RequestParam(value = "qty") Integer qty) {
        Assert.notNull(orderId);
        Assert.notNull(itemId);
        Assert.notNull(qty);

        return orderDao.changeQty(orderId, itemId, qty);
    }

    @RequestMapping("/tender/change")
    @ResponseBody
    public Order changeTender(@RequestParam(value = "order") Integer orderId,
                              @RequestParam(value = "tender") Double tender) {
        Assert.notNull(orderId);
        Assert.notNull(tender);

        Order order = orderDao.findOrder(orderId);
        Assert.isNull(order.getTenderRecord());

        order.setTenderRecord(new TenderRecord(tender, order.getGrandTotal()));
        return order;
    }

}
