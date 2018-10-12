package com.aegeanflow.essentials.database;

import com.aegeanflow.core.plugin.datasource.DatasourceFactory;
import com.aegeanflow.core.spi.node.AbstractSynchronizedNode;
import com.aegeanflow.core.spi.parameter.Input;
import com.aegeanflow.core.spi.parameter.Output;
import com.aegeanflow.core.spi.parameter.Parameter;
import com.google.inject.Inject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Arrays;
import java.util.Collection;

public class DatabaseConnectionNode extends AbstractSynchronizedNode {

    public final static Input<String> DRIVER_CLASS = Parameter.input("driver_class", String.class);
    public final static Input<String> JDBC_URL = Parameter.input("jdbc_url", String.class);
    public final static Input<String> USERNAME = Parameter.input("username", String.class);
    public final static Input<String> PASSWORD = Parameter.input("password", String.class);
    public final static Output<Connection> CONNECTION = Parameter.output("connection", Connection.class);

    private String driverClass;
    private String jdbcUrl;
    private String username;
    private String password;

    private final DatasourceFactory datasourceFactory;

    @Inject
    public DatabaseConnectionNode(DatasourceFactory datasourceFactory) {
        this.datasourceFactory = datasourceFactory;
    }

    @Override
    protected void run() throws Exception{
        Class.forName(driverClass);
        Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
        router.next(CONNECTION, connection);
    }

    @Override
    protected <T> void setInput(Input<T> input, T value) {
        if (input.equals(DRIVER_CLASS)) {
            driverClass = (String) value;
        } else if (input.equals(JDBC_URL)) {
            jdbcUrl = (String) value;
        } else if (input.equals(USERNAME)) {
            username = (String) value;
        } else if (input.equals(PASSWORD)) {
            password = (String) value;
        }
    }

    @Override
    public String getName() {
        return "DatabaseConnection";
    }

    @Override
    public Collection<Output<?>> getOutputs() {
        return Arrays.asList(
            CONNECTION
        );
    }

    @Override
    public Collection<Input<?>> getInputs() {
        return Arrays.asList(
            DRIVER_CLASS,
            JDBC_URL,
            USERNAME,
            PASSWORD
        );
    }
}
