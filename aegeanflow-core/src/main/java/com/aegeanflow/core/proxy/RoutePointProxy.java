package com.aegeanflow.core.proxy;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public class RoutePointProxy {

    private final UUID uuid;

    private final String parameter;

    @JsonCreator
    public RoutePointProxy(
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
