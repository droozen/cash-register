package com.roozen.register.controller;

import com.roozen.register.dao.ItemDao;
import com.roozen.register.dao.OrderDao;
import com.roozen.register.model.Order;
import com.roozen.register.model.OrderHeader;
import com.roozen.register.model.TenderRecord;
import com.roozen.register.model.view.OrderView;
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
    public OrderView newOrder() {
        return new OrderView(orderDao.createNewOrder());
    }

    @RequestMapping("/item/add")
    @ResponseBody
    public OrderView addItem(@RequestParam(value = "order") Integer orderId,
                         @RequestParam(value = "item") Integer itemId) {
        Assert.notNull(orderId);
        Assert.notNull(itemId);

        return new OrderView(orderDao.addItem(orderId, itemId));
    }

    @RequestMapping("/item/remove")
    @ResponseBody
    public OrderView removeItem(@RequestParam(value = "order") Integer orderId,
                            @RequestParam(value = "item") Integer itemId) {
        Assert.notNull(orderId);
        Assert.notNull(itemId);

        return new OrderView(orderDao.removeItem(orderId, itemId));
    }

    @RequestMapping("/find")
    @ResponseBody
    public OrderView findOrder(@RequestParam(value = "order") Integer orderId) {
        Assert.notNull(orderId);

        return new OrderView(orderDao.findOrder(orderId));
    }

    @RequestMapping("/list")
    @ResponseBody
    public List<OrderHeader> list() {
        return orderDao.findAllOrders();
    }

    // TODO: No requirement to be able to change qty in this manner. Is this API needed?
    @RequestMapping("/item/change")
    @ResponseBody
    public OrderView changeItem(@RequestParam(value = "order") Integer orderId,
                            @RequestParam(value = "item") Integer itemId,
                            @RequestParam(value = "qty") Integer qty) {
        Assert.notNull(orderId);
        Assert.notNull(itemId);
        Assert.notNull(qty);

        return new OrderView(orderDao.changeQty(orderId, itemId, qty));
    }

    @RequestMapping("/tender/change")
    @ResponseBody
    public OrderView changeTender(@RequestParam(value = "order") Integer orderId,
                              @RequestParam(value = "tender") Double tender) {
        // TODO: I may not need the asserts. I think spring-boot takes care of these assertions for me.
        Assert.notNull(orderId);
        Assert.notNull(tender);

        Order order = orderDao.findOrder(orderId);
        Assert.isNull(order.getTenderRecord());

        order.setTenderRecord(new TenderRecord(tender, order.getGrandTotal()));
        return new OrderView(order);
    }

    @RequestMapping("/complete")
    @ResponseBody
    public OrderView completeOrder(@RequestParam(value = "order") Integer orderId,
                                   @RequestParam(value = "tender") Double tender) {
        Assert.notNull(orderId);
        Assert.notNull(tender);

        return new OrderView(orderDao.completeOrder(orderId, tender));
    }

}
