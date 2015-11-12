package com.roozen.register.service;

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
@RequestMapping("/service/tender")
public class TenderService {

    @Autowired
    OrderDao orderDao;

    @RequestMapping("/change")
    @ResponseBody
    public Order changeTender(@RequestParam(value = "order") Integer orderId,
                              @RequestParam(value = "tender") Double tender) {
        Assert.notNull(orderId);
        Assert.notNull(tender);

        Order order = orderDao.findOrder(orderId);

        order.setTenderRecord(new TenderRecord(tender, order.getGrandTotal()));
        return order;
    }

    @RequestMapping("/complete")
    @ResponseBody
    public Order completeOrder(@RequestParam(value = "order") Integer orderId,
                               @RequestParam(value = "tender") Double tender) {
        Assert.notNull(orderId);
        Assert.notNull(tender);

        // TODO: Consider moving this method to a TenderDao
        return orderDao.completeOrder(orderId, tender);
    }
}
