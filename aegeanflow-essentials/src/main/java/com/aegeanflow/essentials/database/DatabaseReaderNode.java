package com.aegeanflow.essentials.database;

import com.aegeanflow.core.route.tunnel.StreamTunnel;
import com.aegeanflow.core.spi.node.AbstractSynchronizedNode;
import com.aegeanflow.core.spi.parameter.Input;
import com.aegeanflow.core.spi.parameter.Output;
import com.aegeanflow.core.spi.parameter.Parameter;
import com.aegeanflow.essentials.table.RandomAccessTable;
import com.aegeanflow.essentials.table.Row;
import com.aegeanflow.essentials.table.Table;

import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Arrays;
import java.util.Collection;

public class DatabaseReaderNode extends AbstractSynchronizedNode {

    public static final Input<Connection> CONNECTION = Parameter.input("connection", Connection.class);
    public static final Input<String> QUERY = Parameter.input("query", String.class);
    public static final Output<Table> TABLE = Parameter.output("table", Table.class);

    private Connection connection;
    private String query;

    @Override
    protected void run() throws Exception{
        ResultSet resultSet = connection.createStatement().executeQuery(query);
        Table table = new RandomAccessTable(DatabaseUtil.createSchema(resultSet));
        try(StreamTunnel<Row> stream = router.next(TABLE, table)) {
            int k = 0;
            while (resultSet.next() && k < 1000) {
                k++;
                Object[] values = new Object[resultSet.getMetaData().getColumnCount()];
                for (int i = 0; i < resultSet.getMetaData().getColumnCount(); i++) {
                    values[i] = resultSet.getObject(i + 1);
                }
                stream.send(new Row(values));
            }
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
        return "Database Reader";
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
