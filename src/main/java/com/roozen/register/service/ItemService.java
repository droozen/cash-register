package com.roozen.register.service;

import com.roozen.register.dao.ItemDao;
import com.roozen.register.model.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/service/item")
public class ItemService {

    @Autowired
    ItemDao itemDao;

    @RequestMapping("/list")
    @ResponseBody
    public List<Item> list() {
        return itemDao.findAllItems();
    }
}
