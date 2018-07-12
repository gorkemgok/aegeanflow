package com.aegeanflow.core;

public class Route<O extends I, I> {

    private final OutputPoint<O> source;

    private final InputPoint<I> target;

    public Route(OutputPoint<O> source, InputPoint<I> target) {
        this.source = source;
        this.target = target;
    }

    public OutputPoint<O> getSource() {
        return source;
    }

    public InputPoint<I> getTarget() {
        return target;
    }

    public boolean isOutputOf(Node node, Output<?> parameter) {
        return source.getNode().equals(node) && source.getOutput().isAssignable(parameter);
    }
}
