package com.roozen.register.controller;

import com.roozen.register.dao.ItemDao;
import com.roozen.register.model.Item;
import com.roozen.register.model.view.ItemView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/controller/item")
public class ItemController {

    @Autowired
    ItemDao itemDao;

    @RequestMapping("/list")
    @ResponseBody
    public List<ItemView> list() {
        List<ItemView> itemViews = new ArrayList<>();
        List<Item> items = itemDao.findAllItems();
        for (Item item : items) {
            itemViews.add(new ItemView(item));
        }
        return itemViews;
    }
}
