package com.aegeanflow.essentials.box;

import com.aegeanflow.core.exchange.Exchange;
import com.aegeanflow.core.spi.annotation.NodeInput;
import com.aegeanflow.core.spi.box.AbstractAnnotatedBox;
import com.aegeanflow.core.spi.annotation.NodeEntry;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.util.Properties;

import static com.aegeanflow.core.box.definition.BoxIODefinition.InputType.CONGIF;

/**
 * Created by gorkem on 12.01.2018.
 */
@NodeEntry(name = "Database Connection")
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
        return Exchange.createNonpersistent(driver.connect(jdbcUrl, properties));
    }

    @NodeInput(label = "Driver Class", order = 1, inputType = CONGIF)
    public void setJdbcClass(String jdbcClass) {
        this.jdbcClass = jdbcClass;
    }

    @NodeInput(label = "Database Url", order = 2, inputType = CONGIF)
    public void setJdbcUrl(String jdbcUrl) {
        this.jdbcUrl = jdbcUrl;
    }

    @NodeInput(label = "Database Username", order = 3, inputType = CONGIF)
    public void setUser(String user) {
        this.user = user;
    }

    @NodeInput(label = "Database Password", order = 4, inputType = CONGIF)
    public void setPassword(String password) {
        this.password = password;
    }
}
