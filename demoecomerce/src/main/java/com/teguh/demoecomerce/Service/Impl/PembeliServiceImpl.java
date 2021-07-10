package com.teguh.demoecomerce.Service.Impl;

import com.teguh.demoecomerce.Dao.PembeliDao;
import com.teguh.demoecomerce.Models.Items;
import com.teguh.demoecomerce.Models.Pembeli;
import com.teguh.demoecomerce.Service.PembeliService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("PembeliService")
public class PembeliServiceImpl implements PembeliService {
    @Autowired
    PembeliDao pembelidao;

    @Override
    public Pembeli search(String nama) {
        Pembeli mpembeli = new Pembeli();
        mpembeli = pembelidao.getBynama(nama);
        return mpembeli;
    }
}
