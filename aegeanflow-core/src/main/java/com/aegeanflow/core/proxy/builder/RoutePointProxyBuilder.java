package com.aegeanflow.core.proxy.builder;

import com.aegeanflow.core.proxy.RoutePointProxy;

import java.util.UUID;

public final class RoutePointProxyBuilder {
    private UUID uuid;
    private String parameter;

    private RoutePointProxyBuilder() {
    }

    public static RoutePointProxyBuilder aRoutePointProxy() {
        return new RoutePointProxyBuilder();
    }

    public RoutePointProxyBuilder withUUID(UUID uuid) {
        this.uuid = uuid;
        return this;
    }

    public RoutePointProxyBuilder withParameter(String parameter) {
        this.parameter = parameter;
        return this;
    }

    public RoutePointProxy build() {
        return new RoutePointProxy(uuid, parameter);
    }
}
