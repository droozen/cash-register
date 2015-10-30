package com.roozen.register.controller;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@EnableAutoConfiguration
@RequestMapping("/order")
public class OrderController {

    @RequestMapping("/new")
    @ResponseBody
    public Order newOrder() {
        return new Order();
    }

    class Order {
        String orderNumber = "10";

        public String getOrderNumer() {
            return orderNumber;
        }
    }

}
