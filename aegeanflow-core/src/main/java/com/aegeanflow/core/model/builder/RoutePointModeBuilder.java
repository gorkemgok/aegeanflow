package com.aegeanflow.core.model.builder;

import com.aegeanflow.core.model.RoutePointModel;

import java.util.UUID;

public final class RoutePointModeBuilder {
    private UUID uuid;
    private String parameter;

    private RoutePointModeBuilder() {
    }

    public static RoutePointModeBuilder aRoutePointProxy() {
        return new RoutePointModeBuilder();
    }

    public RoutePointModeBuilder withUUID(UUID uuid) {
        this.uuid = uuid;
        return this;
    }

    public RoutePointModeBuilder withParameter(String parameter) {
        this.parameter = parameter;
        return this;
    }

    public RoutePointModel build() {
        return new RoutePointModel(uuid, parameter);
    }
}
