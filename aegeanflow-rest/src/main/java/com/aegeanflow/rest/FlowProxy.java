package com.aegeanflow.rest;

import com.aegeanflow.core.flow.FlowNode;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;
import java.util.UUID;

/**
 * Created by gorkem on 22.01.2018.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class FlowProxy {
    private UUID uuid;
    private List<FlowNode> nodeList;
    private List<FlowConnectionProxy> connectionList;

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public List<FlowNode> getNodeList() {
        return nodeList;
    }

    public void setNodeList(List<FlowNode> nodeList) {
        this.nodeList = nodeList;
    }

    public List<FlowConnectionProxy> getConnectionList() {
        return connectionList;
    }

    public void setConnectionList(List<FlowConnectionProxy> connectionList) {
        this.connectionList = connectionList;
    }
}
