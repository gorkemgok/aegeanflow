package com.aegeanflow.core.ioc;

import com.aegeanflow.core.route.tunnel.FlowTunnel;
import com.aegeanflow.core.route.tunnel.FlowTunnelListener;
import com.aegeanflow.core.route.tunnel.rest.RestFlowTunnel;
import com.aegeanflow.core.route.tunnel.rest.RestFlowTunnelListener;
import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.google.inject.multibindings.Multibinder;

public class TunnelModule extends AbstractModule {
    @Override
    protected void configure() {
        Multibinder<FlowTunnel> flowTunnelMultibinder = Multibinder.newSetBinder(binder(), FlowTunnel.class);
        flowTunnelMultibinder.addBinding().to(RestFlowTunnel.class).in(Scopes.SINGLETON);
        bind(FlowTunnel.class).annotatedWith(Default.class).to(RestFlowTunnel.class).in(Scopes.SINGLETON);
        bind(FlowTunnelListener.class).to(RestFlowTunnelListener.class).in(Scopes.SINGLETON);
    }
}
