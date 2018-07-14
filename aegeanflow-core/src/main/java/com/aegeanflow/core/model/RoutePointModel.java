package com.aegeanflow.core.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public class RoutePointModel extends IdModel {

    private final UUID uuid;

    private final String parameter;

    @JsonCreator
    public RoutePointModel(
            @JsonProperty("uuid") UUID uuid,
            @JsonProperty("parameter") String parameter) {
        this.uuid = uuid;
        this.parameter = parameter;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getParameter() {
        return parameter;
    }
}
