package com.aegeanflow.core.flow;

import java.util.Map;
import java.util.UUID;

/**
 * Created by gorkem on 12.01.2018.
 */
public class FlowNode {

    private UUID uuid;

    private String nodeClass;

    private Map<String, Object> configuration;

    public Map<String, Object> getConfiguration() {
        return configuration;
    }

    public void setConfiguration(Map<String, Object> configuration) {
        this.configuration = configuration;
    }

    public UUID getUUID() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getNodeClass() {
        return nodeClass;
    }

    public void setNodeClass(String nodeClass) {
        this.nodeClass = nodeClass;
    }
}
