package com.mjc.stage2.impl;

import com.mjc.stage2.ConnectionFactory;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class H2ConnectionFactory implements ConnectionFactory {

    private static volatile H2ConnectionFactory instance;
    private String driver;
    private String url;
    private String name;
    private String password;

    private H2ConnectionFactory(String driver, String url, String password, String name) {
        this.driver = driver;
        this.url = url;
        this.name = name;
        this.password = password;
    }

    public H2ConnectionFactory() {
        if (instance == null) {
            Properties properties = new Properties();
            try (InputStream input = H2ConnectionFactory.class.getClassLoader().
                    getResourceAsStream("h2database.properties")) {
                properties.load(input);
                instance = new H2ConnectionFactory(properties.getProperty("jdbc_driver"),
                        properties.getProperty("db_url"),
                        properties.getProperty("password"),
                        properties.getProperty("user"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private Connection getConnection() throws SQLException {
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try {
            return DriverManager.getConnection(url, name, password);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Connection createConnection() throws SQLException {
        return instance.getConnection();
    }
}

