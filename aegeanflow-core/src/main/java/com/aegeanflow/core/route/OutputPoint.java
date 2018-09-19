package com.aegeanflow.core.route;

import com.aegeanflow.core.deployment.Deployment;
import com.aegeanflow.core.spi.node.Node;
import com.aegeanflow.core.spi.parameter.Output;

public class OutputPoint<T> {

    private final Node node;

    private final Output<T> output;

    private final Deployment deployment;

    public OutputPoint(Node node, Output<T> output, Deployment deployment) {
        this.node = node;
        this.output = output;
        this.deployment = deployment;
    }

    public Node getNode() {
        return node;
    }

    public Output<T> getOutput() {
        return output;
    }

    public Deployment getDeployment() {
        return deployment;
    }
}
