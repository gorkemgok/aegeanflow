package com.aegeanflow.essentials.box;

import com.aegeanflow.core.Exchange;
import com.aegeanflow.core.spi.AbstractAnnotatedBox;
import com.aegeanflow.core.spi.annotation.NodeConfig;
import com.aegeanflow.core.spi.annotation.NodeEntry;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.util.Properties;

/**
 * Created by gorkem on 12.01.2018.
 */
@NodeEntry(label = "Database Connection")
public class DatabaseConnectionBox extends AbstractAnnotatedBox<Connection> {

    private String jdbcClass;

    private String jdbcUrl;

    private String user = "";

    private String password = "";

    @Override
    public Exchange<Connection> call() throws Exception {
        Driver driver = DriverManager.getDriver(jdbcUrl);
        Properties properties = new Properties();
        properties.setProperty("user", user);
        properties.setProperty("password", password);
        return Exchange.create(driver.connect(jdbcUrl, properties));
    }

    @NodeConfig(label = "Driver Class", order = 1)
    public void setJdbcClass(String jdbcClass) {
        this.jdbcClass = jdbcClass;
    }

    @NodeConfig(label = "Database Url", order = 2)
    public void setJdbcUrl(String jdbcUrl) {
        this.jdbcUrl = jdbcUrl;
    }

    @NodeConfig(label = "Database Username", order = 3)
    public void setUser(String user) {
        this.user = user;
    }

    @NodeConfig(label = "Database Password", order = 4)
    public void setPassword(String password) {
        this.password = password;
    }
}