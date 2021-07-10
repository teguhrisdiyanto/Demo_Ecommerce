package com.teguh.demoecomerce.Service.Impl;

import com.teguh.demoecomerce.Dao.ItemsDao;
import com.teguh.demoecomerce.Models.Items;
import com.teguh.demoecomerce.Service.ItemsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service("ItemsService")
public class ItemsServiceImpl implements ItemsService {

    @Autowired
    ItemsDao itemsDao;

    @Override
    public Items getBynama(String nama) {
        Items mitems = new Items();
        mitems = itemsDao.getBynama(nama);
        return mitems;
    }
}
