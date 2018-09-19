package com.aegeanflow.core.resource;

import com.aegeanflow.core.route.tunnel.StreamTunnel;
import com.aegeanflow.core.spi.node.AbstractNode;
import com.aegeanflow.core.spi.parameter.Input;
import com.aegeanflow.core.spi.parameter.Output;
import com.aegeanflow.core.spi.parameter.Parameter;
import com.aegeanflow.core.table.*;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class RandomTableGeneratorNode extends AbstractNode {

    public final static Output<Table> MAIN_OUTPUT = Parameter.output("main", Table.class);

    @Override
    protected void run() {
        List<Field> fieldList = Arrays.asList(
            new Field("f1", Integer.TYPE),
            new Field("f2", Double.TYPE),
            new Field("f3", String.class)
        );
        Schema schema = new Schema(fieldList);
        RandomAccessTable table = new RandomAccessTable(schema);

        try(StreamTunnel<Row> streamTunnel = router.next(MAIN_OUTPUT, table)) {
            for (int i = 0; i < 100; i++) {
                streamTunnel.send(new Row(1, 1.1, "test"));
            }
        }
    }

    @Override
    protected <T> void setInput(Input<T> input, T value) {}

    @Override
    public String getName() {
        return "RandomTableGenerator";
    }

    @Override
    public Collection<Output<?>> getOutputs() {
        return Arrays.asList(MAIN_OUTPUT);
    }

    @Override
    public Collection<Input<?>> getInputs() {
        return Collections.EMPTY_LIST;
    }

    @Override
    public <T> void accept(Input<T> input, T value) {}
}
