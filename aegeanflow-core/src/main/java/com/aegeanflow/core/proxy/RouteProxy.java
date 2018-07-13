package com.aegeanflow.core.proxy;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

/**
 * Created by gorkem on 12.01.2018.
 */
public class RouteProxy {

    public enum Type {
        FLOW, WAIT
    }

    private final String label;

    private final UUID uuid;

    private final Type type;

    private final RoutePointProxy source;

    private final RoutePointProxy target;

    @JsonCreator
    public RouteProxy(
        @JsonProperty("label") String label,
        @JsonProperty("uuid") UUID uuid,
        @JsonProperty("type") Type type,
        @JsonProperty("source") RoutePointProxy source,
        @JsonProperty("target") RoutePointProxy target) {
        this.label = label;
        this.uuid = uuid;
        this.type = type;
        this.source = source;
        this.target = target;
    }

    public UUID getUuid() {
        return uuid;
    }

    public Type getType() {
        return type;
    }

    public RoutePointProxy getSource() {
        return source;
    }

    public RoutePointProxy getTarget() {
        return target;
    }

    public String getLabel() {
        return label;
    }
}
