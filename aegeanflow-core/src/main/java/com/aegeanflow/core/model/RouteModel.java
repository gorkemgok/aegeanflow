package com.aegeanflow.core.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

/**
 * Created by gorkem on 12.01.2018.
 */
public class RouteModel extends IdModel {

    public enum Type {
        FLOW, WAIT
    }

    private final String label;

    private final UUID uuid;

    private final Type type;

    private final RoutePointModel source;

    private final RoutePointModel target;

    @JsonCreator
    public RouteModel(
        @JsonProperty("label") String label,
        @JsonProperty("uuid") UUID uuid,
        @JsonProperty("type") Type type,
        @JsonProperty("source") RoutePointModel source,
        @JsonProperty("target") RoutePointModel target) {
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

    public RoutePointModel getSource() {
        return source;
    }

    public RoutePointModel getTarget() {
        return target;
    }

    public String getLabel() {
        return label;
    }
}
