package com.teguh.demoecomerce.Dao.Impl;

import com.teguh.demoecomerce.Dao.ChartDao;
import com.teguh.demoecomerce.Models.Chart;
import com.teguh.demoecomerce.Models.Checkout;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;


@Repository("ChartDao")
public class ChartDaoImpl implements ChartDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private Logger logger = Logger.getLogger(this.getClass());

    private static final String SQL_INSERT = "INSERT INTO chart VALUES(?,?,?)";
    private static final String SQL_DELETE = "DELETE FROM chart WHERE items_iditems = ? and pembeli_idpembeli =? ";
    private static final String SQL_SELECT_CHART = "select i.nama, i.harga, c.jumlah from chart c " +
            "INNER JOIN items i ON i.iditems = c.items_iditems " +
            "INNER JOIN pembeli p ON  p.idpembeli = c.pembeli_idpembeli " +
            "WHERE c.pembeli_idpembeli = ?";

    @Override
    public int insert(Chart object) {
        int oke = 0;
        try {

            int id_items, idpembeli, jumlah;

            id_items = object.getId_iems();
            idpembeli = object.getId_pembeli();
            jumlah = object.getJumlah();

            oke = jdbcTemplate.update(SQL_INSERT, new Object[]{
                    id_items,
                    idpembeli,
                    jumlah,
            });
            return oke;
        }catch (Exception e){
            logger.error("cannot insert chart : " + e.getMessage());
            return oke;
        }

    }

    @Override
    public List<Checkout> Listcheckout(String id) {
        List<Checkout> listrequest = null;
        try {
            listrequest = jdbcTemplate.query(SQL_SELECT_CHART, new Object[]{id}, new RowMapper<Checkout>() {
                @Override
                public Checkout mapRow(ResultSet rs, int i) throws SQLException {
                    Checkout mcheckout = new Checkout();
                    mcheckout.setNama(rs.getString("nama"));
                    mcheckout.setHarga(rs.getString("harga"));
                    mcheckout.setJumlah(rs.getInt("jumlah"));
                    mcheckout.setTotalharga(Integer.parseInt(rs.getString("harga")) * rs.getInt("jumlah"));
                    return mcheckout;
                }
            });
            return listrequest;
        }catch (Exception e){
            logger.error("items not found dao : " + e.getMessage());
            return listrequest;
        }

    }


    @Override
    public int deletechart(Integer id,Integer idpembeli) {
        int oke = 0;
        try {
            oke = jdbcTemplate.update(SQL_DELETE, new Object[]{id,idpembeli});
            return oke;
        }catch (Exception e){
            logger.error(e.getMessage());
            return oke;
        }

    }
}
