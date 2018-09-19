package com.aegeanflow.core.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collection;
import java.util.UUID;

/**
 * Created by gorkem on 12.01.2018.
 */
public class SessionModel extends IdModel{

    private final UUID uuid;

    private final Collection<NodeModel> nodes;

    private final Collection<RouteModel> routes;

    private final String title;

    @JsonCreator
    public SessionModel(
        @JsonProperty("uuid")  UUID uuid,
        @JsonProperty("title") String title,
        @JsonProperty("nodes") Collection<NodeModel> nodes,
        @JsonProperty("routes") Collection<RouteModel> routes) {
        this.uuid = uuid;
        this.nodes = nodes;
        this.routes = routes;
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public Collection<NodeModel> getNodes() {
        return nodes;
    }

    public Collection<RouteModel> getRoutes() {
        return routes;
    }
}
