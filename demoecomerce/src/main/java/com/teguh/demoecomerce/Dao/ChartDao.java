package com.teguh.demoecomerce.Dao;

import com.teguh.demoecomerce.Models.Chart;
import com.teguh.demoecomerce.Models.Checkout;

import java.util.List;

public interface ChartDao {

    public int insert(Chart object);

    public List<Checkout> Listcheckout (String id);

    public int deletechart (Integer id, Integer idpembeli);
}
