package com.aegeanflow.core.proxy;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.UUID;

/**
 * Created by gorkem on 12.01.2018.
 */
public class SessionProxy {

    private final List<NodeProxy> nodeList;

    private final List<RouteProxy> connectionList;

    private final UUID uuid;

    private final String title;

    @JsonCreator
    public SessionProxy(
            @JsonProperty UUID uuid,
            @JsonProperty String title,
            @JsonProperty List<NodeProxy> nodeList,
            @JsonProperty List<RouteProxy> connectionList) {
        this.nodeList = nodeList;
        this.connectionList = connectionList;
        this.uuid = uuid;
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public UUID getUuid() {
        return uuid;
    }

    public List<NodeProxy> getNodeList() {
        return nodeList;
    }

    public List<RouteProxy> getConnectionList() {
        return connectionList;
    }
}
