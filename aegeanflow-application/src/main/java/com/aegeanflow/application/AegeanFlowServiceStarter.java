package com.aegeanflow.application;

import com.aegeanflow.core.spi.AegeanFlowService;
import com.google.inject.Inject;

import java.util.Set;

/**
 * Created by gorkem on 15.01.2018.
 */
public class AegeanFlowServiceStarter {

    private final Set<AegeanFlowService> services;

    @Inject
    public AegeanFlowServiceStarter(Set<AegeanFlowService> services) {
        this.services = services;
    }

    public void start(){
        services.forEach(aegeanFlowService -> aegeanFlowService.run());
    }
}
