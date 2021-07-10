package com.teguh.demoecomerce.Service.Impl;
import com.teguh.demoecomerce.Dao.ChartDao;
import com.teguh.demoecomerce.Dao.ItemsDao;
import com.teguh.demoecomerce.Dao.PembeliDao;
import com.teguh.demoecomerce.Models.Chart;
import com.teguh.demoecomerce.Models.Checkout;
import com.teguh.demoecomerce.Models.Items;
import com.teguh.demoecomerce.Models.Pembeli;
import com.teguh.demoecomerce.Service.ChartService;
import com.teguh.demoecomerce.Utils.Constants;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service("ChartService")
public class ChartServiceImpl implements ChartService {

    @Autowired
    ItemsDao itemsDao;

    @Autowired
    PembeliDao pembeliDao;

    @Autowired
    ChartDao chartDao;

    @Override
    public int insertone(String items,String Jumlah, String pembeli) {
        int oke = 0;
        Items mitems = new Items();
        Pembeli mpembeli = new Pembeli();
        Chart mchart = new Chart();
        mitems = itemsDao.getBynama(items);

        if(mitems != null){
            mpembeli = pembeliDao.getBynama(pembeli);

           if(mpembeli != null){
               mchart.setId_iems(mitems.getId());
               mchart.setId_pembeli(mpembeli.getId());
               mchart.setJumlah(Integer.parseInt(Jumlah));
               oke = chartDao.insert(mchart);
           }else{
               oke = 3;
           }

        } else{
            oke = 2;
        }

        return oke;
    }

    @Override
    public List<String> insertbatch(String insert, String pembeli) {
        int oke = 0;
        Pembeli mpembeli = new Pembeli();
        Chart mchart = new Chart();
        Items mitems = new Items();
        List<String> list = new ArrayList<>();
        mpembeli = pembeliDao.getBynama(pembeli);
        if(mpembeli != null) {
            JSONArray jsonarray = new JSONArray(insert);
            for (int i = 0; i < jsonarray.length(); i++) {
                JSONObject jsonobject = jsonarray.getJSONObject(i);
                String items = jsonobject.getString("items");
                String jumlah = jsonobject.getString("jumlah");
                mitems = itemsDao.getBynama(items);
                if(mitems != null) {
                    mchart.setId_pembeli(mpembeli.getId());
                    mchart.setId_iems(mitems.getId());
                    mchart.setJumlah(Integer.parseInt(jumlah));
                    oke = chartDao.insert(mchart);
                    if(oke>0){

                    }else{
                        list.add("Duplicate items : "+mitems.getNama());
                    }

                }else{
                   list.add(Constants.ITEM_NOT_FOUND+ " : " +items);
                }
            }
            return list;
        }else{
            list.add("notfound");
            return list;
        }



    }

    @Override
    public int DeleteCart(String items, String pembeli) {
        int oke = 0;
        Pembeli mpembeli = new Pembeli();
        Items mitems = new Items();
        List<String> list = new ArrayList<>();
        mpembeli = pembeliDao.getBynama(pembeli);
        if(mpembeli != null) {
            mitems = itemsDao.getBynama(items);
            if(mitems != null){
                oke = chartDao.deletechart(mitems.getId(),mpembeli.getId());
                if(oke>0){
                    oke = 1;
                }else{
                    oke=0;
                }
            }else{
                oke = 2;
            }
        }else{
            oke = 3;
            }
        return oke;
        }

    @Override
    public List<Checkout> listchart(String id) {
        List<Checkout> listcheckout= new ArrayList();
        Pembeli mpembeli = new Pembeli();
        mpembeli = pembeliDao.getBynama(id);
        if(mpembeli != null) {
            listcheckout = chartDao.Listcheckout(String.valueOf(mpembeli.getId()));
        }else{
            listcheckout = null;
        }
        return listcheckout;

    }


}
