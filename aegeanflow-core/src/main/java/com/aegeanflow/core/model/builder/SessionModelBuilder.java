package com.aegeanflow.core.model.builder;

import com.aegeanflow.core.model.NodeModel;
import com.aegeanflow.core.model.RouteModel;
import com.aegeanflow.core.model.SessionModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

public final class SessionModelBuilder {
    private Collection<NodeModel> nodes;
    private Collection<RouteModel> routes;
    private UUID uuid;
    private String title;
    private Long id;

    private SessionModelBuilder() {
        nodes = new ArrayList<>();
        routes = new ArrayList<>();
    }

    public static SessionModelBuilder aSessionProxy() {
        return new SessionModelBuilder();
    }

    public SessionModelBuilder addNode(NodeModel node) {
        this.nodes.add(node);
        return this;
    }

    public SessionModelBuilder addRoute(RouteModel route) {
        this.routes.add(route);
        return this;
    }

    public SessionModelBuilder withTitle(String title) {
        this.title = title;
        return this;
    }

    public SessionModel build() {
        return new SessionModel(title, nodes, routes);
    }
}
