package com.roozen.register.init;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
public class SqlLoader {

    Logger logger = LoggerFactory.getLogger(SqlLoader.class);

    @Autowired
    JdbcTemplate jdbcTemplate;

    private String createItemTableSql;
    private String createOrderTableSql;
    private String createOrderLineItemTableSql;
    private String createTenderRecordTableSql;
    private String createSalesTaxTableSql;
    private String populateSalesTaxSql;

    public void setCreateItemTableSql(String createItemTableSql) {
        this.createItemTableSql = createItemTableSql;
    }

    public void setCreateOrderTableSql(String createOrderTableSql) {
        this.createOrderTableSql = createOrderTableSql;
    }

    public void setCreateOrderLineItemTableSql(String createOrderLineItemTableSql) {
        this.createOrderLineItemTableSql = createOrderLineItemTableSql;
    }

    public void setCreateTenderRecordTableSql(String createTenderRecordTableSql) {
        this.createTenderRecordTableSql = createTenderRecordTableSql;
    }

    public void setCreateSalesTaxTableSql(String createSalesTaxTableSql) {
        this.createSalesTaxTableSql = createSalesTaxTableSql;
    }

    public void setPopulateSalesTaxSql(String populateSalesTaxSql) {
        this.populateSalesTaxSql = populateSalesTaxSql;
    }

    // TODO: This should be tested, preferably in a automated way where we could install/create a new database, run init,
    // TODO: then verify the schema, but for the sake of time on a practice project we can skip that for now.

    /**
     * Initialize the schema for the database needed for the cash-register.
     */
    public void init() {

        try {
            createItemTable();
            createOrderTable();
            createOrderLineItemTable();
            createTenderRecordTable();
            createSalesTaxTable();

            logger.debug("Database Initialized");
        } catch (SQLException e) {
            logSqlException(e);
        }
    }

    private void createItemTable() throws SQLException {
        jdbcTemplate.execute(createItemTableSql);
    }

    private void createOrderTable() throws SQLException {
        jdbcTemplate.execute(createOrderTableSql);
    }

    private void createOrderLineItemTable() throws SQLException {
        jdbcTemplate.execute(createOrderLineItemTableSql);
    }

    private void createTenderRecordTable() throws SQLException {
        jdbcTemplate.execute(createTenderRecordTableSql);
    }

    private void createSalesTaxTable() throws SQLException {
        jdbcTemplate.execute(createSalesTaxTableSql);

        final List<Boolean> populated = new ArrayList<>();
        jdbcTemplate.query("select 1 from sales_tax", new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet resultSet) throws SQLException {
                populated.add(true);
            }
        });
        if (populated.isEmpty() == false) {
            return; // Only one record in this table for now.
        }

        jdbcTemplate.execute(populateSalesTaxSql);
    }

    private void logSqlException(SQLException e) {
        logger.error("Error initializing database", e);
    }

}
