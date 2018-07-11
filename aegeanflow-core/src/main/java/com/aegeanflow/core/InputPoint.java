package com.aegeanflow.core;

public class InputPoint<T> {

    private final Node node;

    private final Input<T> input;

    public InputPoint(Node node, Input<T> input) {
        this.node = node;
        this.input = input;
    }

    public Node getNode() {
        return node;
    }

    public Input<T> getInput() {
        return input;
    }
}
