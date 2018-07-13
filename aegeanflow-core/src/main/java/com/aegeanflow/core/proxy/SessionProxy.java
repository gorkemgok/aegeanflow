package com.aegeanflow.core.proxy;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collection;
import java.util.UUID;

/**
 * Created by gorkem on 12.01.2018.
 */
public class SessionProxy {

    private final Collection<NodeProxy> nodes;

    private final Collection<RouteProxy> routes;

    private final String title;

    @JsonCreator
    public SessionProxy(
            @JsonProperty("title") String title,
            @JsonProperty("nodes") Collection<NodeProxy> nodes,
            @JsonProperty("routes") Collection<RouteProxy> routes) {
        this.nodes = nodes;
        this.routes = routes;
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public Collection<NodeProxy> getNodes() {
        return nodes;
    }

    public Collection<RouteProxy> getRoutes() {
        return routes;
    }
}
