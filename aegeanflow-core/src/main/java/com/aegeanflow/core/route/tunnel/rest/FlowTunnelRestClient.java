package com.aegeanflow.core.route.tunnel.rest;

import com.aegeanflow.core.route.InputPoint;

import java.io.InputStream;

import static java.lang.String.format;

public class FlowTunnelRestClient {

    public <T> void acceptInput(InputPoint<T> inputPoint, InputStream inputStream) {
        String path = format("tunnel/flow/accept/%s/%s/%s",
            inputPoint.getSession().getId(),
            inputPoint.getNode().getId(),
            inputPoint.getInput().name());
        //TODO send
    }
}
