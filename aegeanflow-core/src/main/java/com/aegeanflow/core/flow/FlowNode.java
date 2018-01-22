package com.aegeanflow.core.flow;

import com.aegeanflow.core.spi.Node;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.Map;
import java.util.UUID;

/**
 * Created by gorkem on 12.01.2018.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class FlowNode {

    private UUID uuid;

    private Class<? extends Node> nodeClass;

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

    public void setUUID(UUID uuid) {
        this.uuid = uuid;
    }

    @JsonSerialize(using = ClassSerializer.class)
    @JsonDeserialize(using = ClassDeserializer.class)
    public Class<? extends Node> getNodeClass() {
        return nodeClass;
    }

    public void setNodeClass(Class<? extends Node> nodeClass) {
        this.nodeClass = nodeClass;
    }
}
