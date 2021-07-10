package com.teguh.demoecomerce.Dao.Impl;

import com.teguh.demoecomerce.Dao.PembeliDao;
import com.teguh.demoecomerce.Models.Items;
import com.teguh.demoecomerce.Models.Pembeli;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;

@Repository("PembeliDao")
public class PembeliDaoImpl implements PembeliDao {
    private Logger logger = Logger.getLogger(this.getClass());
    private static final String SQL_SELECT_byname = "SELECT idpembeli,nama,password from pembeli where nama= ?";
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public Pembeli getBynama(String nama) {
        Pembeli mpembeli = new Pembeli();
        try{
            mpembeli = jdbcTemplate.queryForObject(SQL_SELECT_byname, new Object[]{nama}, new RowMapper<Pembeli>() {
                @Override
                public Pembeli mapRow(ResultSet rs, int i) throws SQLException {
                    Pembeli mpembeli = new Pembeli();
                    mpembeli.setId(rs.getInt("idpembeli"));
                    mpembeli.setNama(rs.getString("nama"));
                    mpembeli.setPassword(rs.getString("password"));
                    return mpembeli;
                }
            });
            return mpembeli;
        }catch (Exception e){
            logger.error("pembeli not found dao : " + e.getMessage());
            return null;
        }


    }
}
