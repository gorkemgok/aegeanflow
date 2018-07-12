package com.aegeanflow.essentials.box;

import com.aegeanflow.core.Exchange;
import com.aegeanflow.core.Precondition;
import com.aegeanflow.core.spi.AbstractAnnotatedBox;
import com.aegeanflow.essentials.data.Convertor;
import com.aegeanflow.essentials.data.DataUtil;
import com.aegeanflow.core.spi.annotation.NodeConfig;
import com.aegeanflow.core.spi.annotation.NodeEntry;
import com.aegeanflow.core.spi.annotation.NodeInput;
import com.aegeanflow.essentials.data.TabularData;
import com.aegeanflow.essentials.data.TabularDataExchange;
import com.google.inject.Inject;

import java.sql.Connection;
import java.sql.ResultSet;

/**
 * Created by gorkem on 12.01.2018.
 */
@NodeEntry(label = "Database Reader")
public class DatabaseReaderBox extends AbstractAnnotatedBox<TabularData> {

    private String query;

    private Connection connection;

    private final Convertor<ResultSet, TabularData> convertor;

    @Inject
    public DatabaseReaderBox(Convertor<ResultSet, TabularData> convertor) {
        this.convertor = convertor;
    }

    @Override
    public Exchange<TabularData> call() throws Exception {
        Precondition.checkNotNullInput(connection, "Connection", this);
        ResultSet resultSet = connection.createStatement().executeQuery(query);
        return new TabularDataExchange(DataUtil.convert(resultSet));
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
