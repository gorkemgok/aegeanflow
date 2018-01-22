package com.aegeanflow.rest;

import com.aegeanflow.core.flow.FlowConnection;
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
    private List<FlowConnection> connectionList;

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

    public List<FlowConnection> getConnectionList() {
        return connectionList;
    }

    public void setConnectionList(List<FlowConnection> connectionList) {
        this.connectionList = connectionList;
    }
}
