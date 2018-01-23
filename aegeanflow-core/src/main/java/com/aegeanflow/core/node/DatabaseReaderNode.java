package com.aegeanflow.core.node;

import com.aegeanflow.core.Precondition;
import com.aegeanflow.core.node.data.Convertor;
import com.aegeanflow.core.spi.AbstractNode;
import com.aegeanflow.core.spi.annotation.NodeConfig;
import com.aegeanflow.core.spi.annotation.NodeEntry;
import com.aegeanflow.core.spi.annotation.NodeInput;
import com.aegeanflow.core.node.data.TabularData;
import com.google.inject.Inject;

import java.sql.Connection;
import java.sql.ResultSet;

/**
 * Created by gorkem on 12.01.2018.
 */
@NodeEntry(label = "Database Reader")
public class DatabaseReaderNode extends AbstractNode<TabularData> {

    private String query;

    private Connection connection;

    private final Convertor<ResultSet, TabularData> convertor;

    @Inject
    public DatabaseReaderNode(Convertor<ResultSet, TabularData> convertor) {
        this.convertor = convertor;
    }

    @Override
    public TabularData call() throws Exception {
        Thread.sleep(5000);
        Precondition.checkNotNullInput(connection, "Connection", this);
        ResultSet resultSet = connection.createStatement().executeQuery(query);

        while (resultSet.next()){
            System.out.println(resultSet.getString(1));
        }
        return null;
    }

    @NodeConfig(label = "Query")
    public void setQuery(String query) {
        this.query = query;
    }

    @NodeInput
    public void setConnection(Connection connection) {
        this.connection = connection;
    }
}
