package com.roozen.register.controller;

import com.roozen.register.dao.ItemDao;
import com.roozen.register.dao.OrderDao;
import com.roozen.register.model.Order;
import com.roozen.register.model.OrderHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/controller/order")
// TODO: Consider splitting into two controllers, one for fetching data and one for changing data
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

    @RequestMapping("/list")
    @ResponseBody
    public List<OrderHeader> list() {
        return orderDao.findAllOrders();
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

    @RequestMapping("/assign")
    @ResponseBody
    public Boolean assignOrderNumber(@RequestParam(value = "order") Integer orderId) {
        Assert.notNull(orderId);

        return orderDao.assignOrderNumber(orderId);
    }

}
