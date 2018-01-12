package com.aegeanflow.core.flow;

import java.util.List;

/**
 * Created by gorkem on 12.01.2018.
 */
public class Flow {

    private List<FlowNode> nodeList;

    private List<FlowConnection> connectionList;

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
