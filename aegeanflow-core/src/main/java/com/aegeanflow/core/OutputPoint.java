package com.aegeanflow.core;

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
