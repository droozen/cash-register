package com.roozen.register.service;

import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.util.ResourceBundle;

@Service
public class SqlService {

    private ResourceBundle properties;

    private String user;
    private String password;
    private String sqlClass;
    private String jdbcUrl;

    public SqlService() {
        properties = ResourceBundle.getBundle("sql");
        user = properties.getString("mysql.user");
        password = properties.getString("mysql.password");
        sqlClass = properties.getString("mysql.class");
        jdbcUrl = properties.getString("mysql.jdbcUrl");
    }

    public void init() {
        System.out.println(sqlClass);
    }

    public Connection getConnection() throws Exception {
        Class.forName(this.sqlClass).newInstance();
        return null;
    }

}
