package com.aegeanflow.core.route;

import com.aegeanflow.core.deployment.Deployment;
import com.aegeanflow.core.session.Session;
import com.aegeanflow.core.spi.node.Node;
import com.aegeanflow.core.spi.parameter.Input;

public class InputPoint<T> {

    private final Session session;

    private final Node node;

    private final Input<T> input;

    private final Deployment deployment;

    public InputPoint(Session session, Node node, Input<T> input, Deployment deployment) {
        this.session = session;
        this.node = node;
        this.input = input;
        this.deployment = deployment;
    }

    public Node getNode() {
        return node;
    }

    public Input<T> getInput() {
        return input;
    }

    public Deployment getDeployment() {
        return deployment;
    }

    public Session getSession() {
        return session;
    }
}
