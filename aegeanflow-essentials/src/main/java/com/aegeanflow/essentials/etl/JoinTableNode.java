package com.aegeanflow.essentials.etl;

import com.aegeanflow.core.route.tunnel.StreamTunnel;
import com.aegeanflow.core.spi.node.AbstractSynchronizedNode;
import com.aegeanflow.core.spi.parameter.Input;
import com.aegeanflow.core.spi.parameter.Output;
import com.aegeanflow.core.spi.parameter.Parameter;
import com.aegeanflow.essentials.table.*;
import com.aegeanflow.essentials.util.RowUtil;
import com.google.inject.TypeLiteral;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class JoinTableNode extends AbstractSynchronizedNode {

    public static final Input<List<String>> IN_JOIN_COLUMNS_1 = Parameter.input("JOIN_COLUMNS_1", new TypeLiteral<List<String>>(){});
    public static final Input<List<String>> IN_JOIN_COLUMNS_2 = Parameter.input("JOIN_COLUMNS_2", new TypeLiteral<List<String>>(){});
    public static final Input<Table> IN_TABLE_1 = Parameter.input("TABLE_1", Table.class);
    public static final Input<Table> IN_TABLE_2 = Parameter.input("TABLE_2", Table.class);
    public static final Output<Table> OUT_MERGED_TABLE = Parameter.output("MERGED_TABLE", Table.class);

    private Table table1;
    private Table table2;
    private List<String> keyList1;
    private List<String> keyList2;

    @Override
    protected void run() throws Exception {
        List<Integer> idx1 = getKeyField1Indexes(keyList1, table1.getSchema());
        List<Integer> idx2 = getKeyField1Indexes(keyList2, table2.getSchema());
        Schema mergedSchema = mergeSchema(table1.getSchema(), table2.getSchema());
        Collection<Row> t1 = table1.collect();
        Iterator<Row> t2 = table2.iterator();
        try(StreamTunnel<Row> stream = router.next(OUT_MERGED_TABLE, new RandomAccessTable(mergedSchema))) {
            while (t2.hasNext()) {
                Row r2 = t2.next();
                Row keyedRow2 = new Row(idx2.stream().map(r2::get).collect(Collectors.toList()));
                t1.stream().forEach(r1 -> {
                    Row keyedRow1 = new Row(idx1.stream().map(r1::get).collect(Collectors.toList()));
                    if (keyedRow1.equals(keyedRow2)) {
                        Row mergedRow = new Row(r1, r2);
                        stream.send(mergedRow);
                    }
                });
            }
        }
    }

    private Schema mergeSchema(Schema schema1, Schema schema2) {
        return new Schema(Stream.of(schema1, schema2)
                .map(Schema::getFieldList)
                .flatMap(List::stream)
                .collect(Collectors.toList()));
    }

    private List<Integer> getKeyField1Indexes(List<String> keyList, Schema schema) {
        return keyList.stream()
                .map(schema::getFieldIndex)
                .filter(i -> i > -1)
                .collect(Collectors.toList());
    }

    @Override
    protected <T> void setInput(Input<T> input, T value) {
        if (input.equals(IN_TABLE_1)) {
            table1 = (Table) value;
        } else if (input.equals(IN_TABLE_2)) {
            table2 = (Table) value;
        } else if (input.equals(IN_JOIN_COLUMNS_1)) {
            keyList1 = (List<String>) value;
        }else if (input.equals(IN_JOIN_COLUMNS_2)) {
            keyList2 = (List<String>) value;
        }
    }

    @Override
    public String getName() {
        return "Table Column Merge";
    }

    @Override
    public Collection<Output<?>> getOutputs() {
        return Arrays.asList(OUT_MERGED_TABLE);
    }

    @Override
    public Collection<Input<?>> getInputs() {
        return Arrays.asList(IN_JOIN_COLUMNS_1, IN_JOIN_COLUMNS_2, IN_TABLE_1, IN_TABLE_2);
    }
}
