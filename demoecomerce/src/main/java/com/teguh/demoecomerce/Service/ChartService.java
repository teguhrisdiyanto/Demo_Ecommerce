package com.teguh.demoecomerce.Service;

import com.teguh.demoecomerce.Models.Chart;
import com.teguh.demoecomerce.Models.Checkout;

import java.util.List;

public interface ChartService {

    public int insertone(String items, String jumlah, String pembeli);

    public List<String> insertbatch (String insert , String pembeli);

    public int DeleteCart (String items , String pembeli);

    public List<Checkout> listchart (String id);
}
