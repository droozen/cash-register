package com.roozen.register.controller;

import com.roozen.register.dao.ItemDao;
import com.roozen.register.model.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/controller/item")
public class ItemController {

    @Autowired
    ItemDao itemDao;

    @RequestMapping("/list")
    @ResponseBody
    public List<Item> list() {
        // Not all controller methods will be a straight pass through like this one.
        // This is where we map the data from the dao layer to the view the UI needs.
        return itemDao.findAllItems();
    }
}
