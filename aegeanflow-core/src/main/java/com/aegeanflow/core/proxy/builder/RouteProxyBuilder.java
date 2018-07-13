package com.aegeanflow.core.proxy.builder;

import com.aegeanflow.core.proxy.RoutePointProxy;
import com.aegeanflow.core.proxy.RouteProxy;

import java.util.UUID;

public final class RouteProxyBuilder {
    private UUID uuid;
    private RouteProxy.Type type;
    private RoutePointProxy source;
    private RoutePointProxy target;
    private String label;

    private RouteProxyBuilder() {
    }

    public static RouteProxyBuilder aRouteProxy() {
        return new RouteProxyBuilder();
    }

    public RouteProxyBuilder withLabel(String label) {
        this.label = label;
        return this;
    }

    public RouteProxyBuilder withUUID(UUID uuid) {
        this.uuid = uuid;
        return this;
    }

    public RouteProxyBuilder withRandomUUID() {
        this.uuid = UUID.randomUUID();
        return this;
    }

    public RouteProxyBuilder withType(RouteProxy.Type type) {
        this.type = type;
        return this;
    }

    public RouteProxyBuilder withSource(RoutePointProxy source) {
        this.source = source;
        return this;
    }

    public RouteProxyBuilder withTarget(RoutePointProxy target) {
        this.target = target;
        return this;
    }

    public RouteProxy build() {
        return new RouteProxy(label, uuid, type, source, target);
    }
}
