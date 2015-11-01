package com.roozen.register.controller;

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
@RequestMapping("/controller/tender")
public class TenderController {

    @Autowired
    OrderDao orderDao;

    @RequestMapping("/change")
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

    @RequestMapping("/complete")
    @ResponseBody
    public Order completeOrder(@RequestParam(value = "order") Integer orderId,
                               @RequestParam(value = "tender") Double tender) {
        Assert.notNull(orderId);
        Assert.notNull(tender);

        return orderDao.completeOrder(orderId, tender);
    }
}
