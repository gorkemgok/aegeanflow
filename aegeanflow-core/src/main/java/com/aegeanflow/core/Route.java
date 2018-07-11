package com.aegeanflow.core;

public class Route<T> {

    private final OutputPoint<T> source;

    private final InputPoint<T> target;

    public Route(OutputPoint<T> source, InputPoint<T> target) {
        this.source = source;
        this.target = target;
    }

    public OutputPoint<T> getSource() {
        return source;
    }

    public InputPoint<T> getTarget() {
        return target;
    }

    public boolean isOutputOf(Node node, Output<?> parameter) {
        return source.getNode().equals(node) && source.getOutput().equals(parameter);
    }
}
