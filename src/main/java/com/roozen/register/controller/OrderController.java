package com.roozen.register.controller;

import com.roozen.register.dao.OrderDao;
import com.roozen.register.model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/controller/order")
public class OrderController {

    @Autowired
    OrderDao orderDao;

    @RequestMapping("/create")
    @ResponseBody
    public Order newOrder() {
        return orderDao.createNewOrder();
    }

    @RequestMapping("/item/add")
    @ResponseBody
    public Order addItem(@RequestParam(value = "order") Integer orderId,
                         @RequestParam(value = "item") Integer itemId) {
        return orderDao.addItem(orderId, itemId);
    }

    @RequestMapping("/find")
    @ResponseBody
    public Order findOrder(@RequestParam(value = "order") Integer orderId) {
        return orderDao.findOrder(orderId);
    }

}
