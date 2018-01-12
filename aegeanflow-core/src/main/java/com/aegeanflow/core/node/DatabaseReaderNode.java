package com.aegeanflow.core.node;

import com.aegeanflow.core.AbstractNode;
import com.aegeanflow.core.annotation.NodeConfig;
import com.aegeanflow.core.annotation.NodeEntry;
import com.aegeanflow.core.annotation.NodeInput;
import com.aegeanflow.core.node.data.QueryResult;

import java.sql.Connection;
import java.sql.ResultSet;

/**
 * Created by gorkem on 12.01.2018.
 */
@NodeEntry(label = "Database Reader")
public class DatabaseReaderNode extends AbstractNode<QueryResult> {

    private String query;

    private Connection connection;

    @Override
    public QueryResult call() throws Exception {
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
