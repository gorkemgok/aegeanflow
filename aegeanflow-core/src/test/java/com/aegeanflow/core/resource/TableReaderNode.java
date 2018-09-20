package com.aegeanflow.core.resource;

import com.aegeanflow.core.route.tunnel.StreamTunnel;
import com.aegeanflow.core.spi.Streamable;
import com.aegeanflow.core.spi.node.AbstractSynchronizedNode;
import com.aegeanflow.core.spi.parameter.Input;
import com.aegeanflow.core.spi.parameter.Output;
import com.aegeanflow.core.spi.parameter.Parameter;
import com.aegeanflow.core.table.RandomAccessTable;
import com.aegeanflow.core.table.Row;
import com.aegeanflow.core.table.Table;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collection;

public class TableReaderNode extends AbstractSynchronizedNode {

    public static final Input<Connection> CONNECTION = Parameter.input("connection", Connection.class);
    public static final Input<String> QUERY = Parameter.input("query", String.class);
    public static final Output<Table> TABLE = Parameter.output("table", Table.class);

    private Connection connection;
    private String query;

    @Override
    protected void run() {
        try {
            ResultSet resultSet = connection.createStatement().executeQuery(query);
            Table table = new RandomAccessTable(null);
            StreamTunnel<Row> stream = router.next(TABLE, table);
            while (resultSet.next()) {
                Object[] values = new Object[resultSet.getMetaData().getColumnCount()];
                for (int i = 0; i < resultSet.getMetaData().getColumnCount(); i++) {
                    values[i] = resultSet.getObject(i+1);
                    stream.send(new Row(values));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected <T> void setInput(Input<T> input, T value) {
        if (input.equals(CONNECTION)) {
            connection = (Connection) value;
        } else if (input.equals(QUERY)){
            query = (String) value;
        }
    }

    @Override
    public String getName() {
        return "TableReader";
    }

    @Override
    public Collection<Output<?>> getOutputs() {
        return Arrays.asList(TABLE);
    }

    @Override
    public Collection<Input<?>> getInputs() {
        return Arrays.asList(CONNECTION, QUERY);
    }
}
