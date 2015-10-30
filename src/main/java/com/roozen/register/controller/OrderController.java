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

    @RequestMapping("/new")
    @ResponseBody
    public Order newOrder() {
        return orderDao.createNewOrder();
    }

    @RequestMapping("/select")
    @ResponseBody
    public Order selectItem(@RequestParam(value = "order") Integer orderId, @RequestParam(value = "item") String itemName) {
        orderDao.addItem(orderId, itemName);
        return orderDao.findOrder(orderId);
    }

}
