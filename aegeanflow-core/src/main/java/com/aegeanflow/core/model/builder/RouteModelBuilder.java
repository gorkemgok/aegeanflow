package com.aegeanflow.core.model.builder;

import com.aegeanflow.core.model.RoutePointModel;
import com.aegeanflow.core.model.RouteModel;

import java.util.UUID;

public final class RouteModelBuilder {
    private UUID uuid;
    private RouteModel.Type type;
    private RoutePointModel source;
    private RoutePointModel target;
    private String label;

    private RouteModelBuilder() {
    }

    public static RouteModelBuilder aRouteProxy() {
        return new RouteModelBuilder();
    }

    public RouteModelBuilder withLabel(String label) {
        this.label = label;
        return this;
    }

    public RouteModelBuilder withUUID(UUID uuid) {
        this.uuid = uuid;
        return this;
    }

    public RouteModelBuilder withRandomUUID() {
        this.uuid = UUID.randomUUID();
        return this;
    }

    public RouteModelBuilder withType(RouteModel.Type type) {
        this.type = type;
        return this;
    }

    public RouteModelBuilder withSource(RoutePointModel source) {
        this.source = source;
        return this;
    }

    public RouteModelBuilder withTarget(RoutePointModel target) {
        this.target = target;
        return this;
    }

    public RouteModel build() {
        return new RouteModel(label, uuid, type, source, target);
    }
}
