package com.aegeanflow.essentials.database;

import com.aegeanflow.core.spi.node.AbstractSynchronizedNode;
import com.aegeanflow.core.spi.parameter.Input;
import com.aegeanflow.core.spi.parameter.Output;
import com.aegeanflow.core.spi.parameter.Parameter;

import java.sql.Connection;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

public class DatabaseExecuteNode extends AbstractSynchronizedNode {

    public static final Input<Connection> CONNECTION = Parameter.input("connection", Connection.class);

    public static final Input<String> QUERY = Parameter.input("query", String.class);


    private Connection connection;

    private String query;

    @Override
    protected void run() throws Exception {
        connection.createStatement().execute(query);
    }

    @Override
    protected <T> void setInput(Input<T> input, T value) {
        if (input.equals(CONNECTION)) {
            connection = (Connection) value;
        }
        if (input.equals(QUERY)) {
            query = (String) value;
        }
    }

    @Override
    public String getName() {
        return "Database Execute";
    }

    @Override
    public Collection<Output<?>> getOutputs() {
        return Collections.EMPTY_LIST;
    }

    @Override
    public Collection<Input<?>> getInputs() {
        return Arrays.asList(CONNECTION, QUERY);
    }
}
