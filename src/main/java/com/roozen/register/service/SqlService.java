package com.roozen.register.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.sql.SQLException;

@Service
public class SqlService {

    @Autowired
    JdbcTemplate jdbcTemplate;

    private String createItemTableSql;

    public void setCreateItemTableSql(String createItemTableSql) {
        this.createItemTableSql = createItemTableSql;
    }

    public void init() {

        try {
            createItemTable();
        } catch (SQLException e) {
            System.out.println("Database initialization failed");
            return;
        }
    }

    private void createItemTable() throws SQLException {
        jdbcTemplate.execute(createItemTableSql);
    }

    private void logSqlException(SQLException e) {
        System.out.println(e.getMessage());
        System.out.println(e.getErrorCode());
        System.out.println(e.getSQLState());
        e.printStackTrace();
    }

    private void close(AutoCloseable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
