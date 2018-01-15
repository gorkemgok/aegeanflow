package com.aegeanflow.rest;

import static spark.Spark.*;
import com.aegeanflow.core.AegeanFlow;
import com.aegeanflow.core.spi.AegeanFlowService;
import com.google.inject.Inject;

import java.util.stream.Collectors;

/**
 * Created by gorkem on 15.01.2018.
 */
public class RestService implements AegeanFlowService {

    private final RestConfig restConfig;

    private final AegeanFlow aegeanFlow;

    private final JsonTransformer jsonTransformer;

    @Inject
    public RestService(RestConfig restConfig, AegeanFlow aegeanFlow, JsonTransformer jsonTransformer) {
        this.restConfig = restConfig;
        this.aegeanFlow = aegeanFlow;
        this.jsonTransformer = jsonTransformer;
    }

    @Override
    public void init() {
        port(restConfig.port);

        staticFiles.location("ui");

        get("node/list", "application/json", (req, res) -> {
            res.type("application/json");
            return aegeanFlow.getNodeInfoList().stream()
                    .map(nodeInfo -> nodeInfo.getNodeClass()).collect(Collectors.toList());
        }, jsonTransformer);
    }

    @Override
    public void run() {
        aegeanFlow.getNodeInfoList().forEach(
                compiledNodeInfo -> System.out.println(compiledNodeInfo.getNodeClass().getName()));
    }

    @Override
    public void terminate() {
        stop();
    }
}
