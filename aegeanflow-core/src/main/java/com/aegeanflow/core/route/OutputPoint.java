package com.aegeanflow.core.route;

import com.aegeanflow.core.spi.node.Node;
import com.aegeanflow.core.spi.parameter.Output;

public class OutputPoint<T> {

    private final Node node;

    private final Output<T> output;

    public OutputPoint(Node node, Output<T> output) {
        this.node = node;
        this.output = output;
    }

    public Node getNode() {
        return node;
    }

    public Output<T> getOutput() {
        return output;
    }
}
