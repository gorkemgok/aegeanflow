package com.aegeanflow.core.route.tunnel;

import com.aegeanflow.core.ioc.Default;
import com.google.inject.Inject;

import java.util.Set;

public class FlowTunnelProvider {

    private final Set<FlowTunnel> flowTunnels;

    private final FlowTunnel defaultFlowTunnel;

    @Inject
    public FlowTunnelProvider(Set<FlowTunnel> flowTunnels, @Default FlowTunnel defaultFlowTunnel) {
        this.flowTunnels = flowTunnels;
        this.defaultFlowTunnel = defaultFlowTunnel;
    }

    public FlowTunnel get(TunnelType type) {
        return flowTunnels.stream()
            .filter(tunnel -> tunnel.getTunnelType().equals(type))
            .findFirst()
            .orElse(defaultFlowTunnel);
    }
}
