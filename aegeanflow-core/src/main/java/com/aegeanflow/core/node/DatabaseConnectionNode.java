package com.aegeanflow.core.node;

import com.aegeanflow.core.AbstractNode;
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
public class DatabaseConnectionNode extends AbstractNode<Connection> {

    private String jdbcClass;

    private String jdbcUrl;

    private String user;

    private String password;

    @Override
    public Connection call() throws Exception {
        Driver driver = DriverManager.getDriver(jdbcUrl);
        Properties properties = new Properties();
        properties.setProperty("user", user);
        properties.setProperty("password", password);
        return driver.connect(jdbcUrl, properties);
    }

    @NodeConfig(label = "Driver Class")
    public void setJdbcClass(String jdbcClass) {
        this.jdbcClass = jdbcClass;
    }

    @NodeConfig(label = "Database Url")
    public void setJdbcUrl(String jdbcUrl) {
        this.jdbcUrl = jdbcUrl;
    }

    @NodeConfig(label = "Database Username")
    public void setUser(String user) {
        this.user = user;
    }

    @NodeConfig(label = "Database Password")
    public void setPassword(String password) {
        this.password = password;
    }
}
