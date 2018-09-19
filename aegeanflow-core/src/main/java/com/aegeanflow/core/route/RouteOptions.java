package com.aegeanflow.core.route;

import com.aegeanflow.core.route.tunnel.TunnelType;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class RouteOptions {

    private final TunnelType tunnelType;

    @JsonCreator
    public RouteOptions(
        @JsonProperty("type") TunnelType tunnelType) {
        this.tunnelType = tunnelType;
    }

    public RouteOptions() {
        this.tunnelType = StandartTunnelType.RESTFUL;
    }

    public TunnelType getTunnelType() {
        return tunnelType;
    }

}
