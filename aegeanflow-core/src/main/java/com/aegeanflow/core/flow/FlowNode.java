package com.aegeanflow.core.flow;

import com.aegeanflow.core.spi.Node;
import com.aegeanflow.core.spi.RunnableNode;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.util.Map;
import java.util.UUID;

/**
 * Created by gorkem on 12.01.2018.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class FlowNode implements Node{

    private String name;

    private UUID uuid;

    private Class<? extends RunnableNode> nodeClass;

    private Map<String, Object> configuration;

    private Double x, y, w, h;
    private String color;

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

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
    @JsonProperty("type")
    public Class<? extends RunnableNode> getNodeClass() {
        return nodeClass;
    }

    public void setNodeClass(Class<? extends RunnableNode> nodeClass) {
        this.nodeClass = nodeClass;
    }

    public Double getX() {
        return x;
    }

    public void setX(Double x) {
        this.x = x;
    }

    public Double getY() {
        return y;
    }

    public void setY(Double y) {
        this.y = y;
    }

    public Double getW() {
        return w;
    }

    public void setW(Double w) {
        this.w = w;
    }

    public Double getH() {
        return h;
    }

    public void setH(Double h) {
        this.h = h;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
