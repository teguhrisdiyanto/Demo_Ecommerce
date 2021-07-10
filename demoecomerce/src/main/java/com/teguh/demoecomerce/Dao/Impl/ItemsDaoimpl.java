package com.teguh.demoecomerce.Dao.Impl;

import com.teguh.demoecomerce.Dao.ItemsDao;
import com.teguh.demoecomerce.Models.Items;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;


@Repository("ItemsDao")
public class ItemsDaoimpl implements ItemsDao {

    private Logger logger = Logger.getLogger(this.getClass());

    private static final String SQL_SELECT_byname = "SELECT iditems,nama,harga from items where nama= ?";

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public Items getBynama(String nama) {
        Items mitems = new Items();
        try{
            mitems =jdbcTemplate.queryForObject(SQL_SELECT_byname, new Object[]{nama}, new RowMapper<Items>() {
                @Override
                public Items mapRow(ResultSet rs, int i) throws SQLException {
                    Items mitems = new Items();
                    mitems.setId(rs.getInt("iditems"));
                    mitems.setNama(rs.getString("nama"));
                    mitems.setHarga(rs.getString("harga"));

                    return mitems;
                }
            });
            return mitems;
        }catch (Exception e){
            logger.error("items not found dao : " + e.getMessage());
            return null;
        }

    }
}
