package com.aegeanflow.core.proxy.builder;

import com.aegeanflow.core.proxy.NodeProxy;
import com.aegeanflow.core.proxy.RouteProxy;
import com.aegeanflow.core.proxy.SessionProxy;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

public final class SessionProxyBuilder {
    private Collection<NodeProxy> nodes;
    private Collection<RouteProxy> routes;
    private UUID uuid;
    private String title;

    private SessionProxyBuilder() {
        nodes = new ArrayList<>();
        routes = new ArrayList<>();
    }

    public static SessionProxyBuilder aSessionProxy() {
        return new SessionProxyBuilder();
    }

    public SessionProxyBuilder addNode(NodeProxy node) {
        this.nodes.add(node);
        return this;
    }

    public SessionProxyBuilder addRoute(RouteProxy route) {
        this.routes.add(route);
        return this;
    }

    public SessionProxyBuilder withTitle(String title) {
        this.title = title;
        return this;
    }

    public SessionProxy build() {
        return new SessionProxy(title, nodes, routes);
    }
}
