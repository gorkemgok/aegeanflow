package com.aegeanflow.essentials.resource;

import com.aegeanflow.core.route.tunnel.StreamTunnel;
import com.aegeanflow.core.spi.node.AbstractSynchronizedNode;
import com.aegeanflow.core.spi.parameter.Input;
import com.aegeanflow.core.spi.parameter.Output;
import com.aegeanflow.core.spi.parameter.Parameter;
import com.aegeanflow.essentials.table.RandomAccessTable;
import com.aegeanflow.essentials.table.Row;
import com.aegeanflow.essentials.table.Table;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class TableGroupByNode extends AbstractSynchronizedNode {

    public static final Input<Table> INPUT_TABLE = Parameter.input("input_table", Table.class);
    public static final Output<Table> OUTPUT_TABLE = Parameter.output("output_table", Table.class);

    private Table table;

    @Override
    protected void run() {
        Table outputTable = new RandomAccessTable(null);
        Map<String, Double> state = new HashMap<>();
        try(StreamTunnel<Row> stream = router.next(OUTPUT_TABLE, outputTable)) {
            for (Row  row : table) {
                String key = row.get(1).toString();
                Double agg = state.get(key);
                Object val = row.get(3);
                if (val != null && agg != null) {
                    agg += Double.valueOf(val.toString());
                } else if (val != null) {
                    agg = Double.valueOf(val.toString());
                }
                state.put(key, agg);
                stream.send(new Row(key, agg));
            }
        }
    }

    @Override
    protected <T> void setInput(Input<T> input, T value) {
        if (input.equals(INPUT_TABLE)) {
            table = (Table) value;
        }
    }

    @Override
    public String getName() {
        return "TableGroupBy";
    }

    @Override
    public Collection<Output<?>> getOutputs() {
        return Arrays.asList(OUTPUT_TABLE);
    }

    @Override
    public Collection<Input<?>> getInputs() {
        return Arrays.asList(INPUT_TABLE);
    }
}
