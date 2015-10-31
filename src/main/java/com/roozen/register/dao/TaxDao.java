package com.roozen.register.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
public class TaxDao {

    @Autowired
    JdbcTemplate jdbcTemplate;

    // TODO: Come up with a better caching mechanism across the board.
    private static Double salesTaxRate;

    public Double findSalesTaxRate() {
        if (salesTaxRate == null) {
            salesTaxRate = jdbcTemplate.queryForObject("select rate from sales_tax", Double.class);
        }
        return salesTaxRate;
    }

}
