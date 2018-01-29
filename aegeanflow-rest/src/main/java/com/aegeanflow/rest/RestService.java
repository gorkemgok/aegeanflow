package com.aegeanflow.rest;

import static spark.Spark.*;
import com.aegeanflow.core.AegeanFlow;
import com.aegeanflow.core.engine.DataFlowEngine;
import com.aegeanflow.core.engine.DataFlowEngineManager;
import com.aegeanflow.core.engine.FlowFuture;
import com.aegeanflow.core.exception.NodeRuntimeException;
import com.aegeanflow.core.flow.Flow;
import com.aegeanflow.core.spi.AegeanFlowService;
import com.aegeanflow.core.workspace.Workspace;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by gorkem on 15.01.2018.
 */
public class RestService implements AegeanFlowService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestService.class);

    private final RestConfig restConfig;

    private final AegeanFlow aegeanFlow;

    private final JsonTransformer jsonTransformer;

    private final Workspace workspace;

    private final DataFlowEngineManager flowManager;

    private final ObjectMapper objectMapper;

    @Inject
    public RestService(RestConfig restConfig, AegeanFlow aegeanFlow, JsonTransformer jsonTransformer,
                       Workspace workspace, DataFlowEngineManager flowManager) {
        this.restConfig = restConfig;
        this.aegeanFlow = aegeanFlow;
        this.jsonTransformer = jsonTransformer;
        this.workspace = workspace;
        this.flowManager = flowManager;
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public void init() {
        port(restConfig.port);

        staticFiles.location("ui");

        path("/rest-api/v1", () -> {
            get("/node/list", "application/json", (req, res) -> {
                res.type("application/json");
                return aegeanFlow.getNodeRepository().getNodeDefinitionList();
            }, jsonTransformer);

            post("/flow",  "application/json", (req, res) -> {
                Flow flow = objectMapper.readValue(req.body(), Flow.class);
                flow = workspace.save(flow);
                return new UUIDProxy(flow.getUuid());
            }, jsonTransformer);

            post("/flow/run",  (req, res) -> {
                try {
                    Flow flow = new ObjectMapper().readValue(req.body(), Flow.class);
                    DataFlowEngine de = flowManager.create(flow);
                    List<FlowFuture> flowFutureList = de.getResultList();
                    for (FlowFuture flowFuture : flowFutureList) {
                        Object object = flowFuture.get();
                        System.out.println(object);
                    }
                    return new UUIDProxy(flow.getUuid());
                } catch (ExecutionException e) {
                    throw (NodeRuntimeException) e.getCause();
                }
            });

            get("/flow/list",  (req, res) -> {
                List<Flow> flowList = workspace.getFlowList();
                return flowList;
            }, jsonTransformer);
        });

        exception(NodeRuntimeException.class, (e, req, res) -> {
            LOGGER.error("UUID: {}. {}",e.getNodeUUID(), e.getMessage());
            res.status(500);
            res.type("application/json");
            try {
                res.body(objectMapper.writeValueAsString(new NodeErrorProxy(e.getNodeUUID(), e.getMessage())));
            } catch (JsonProcessingException e1) {
                e1.printStackTrace();
            }
        });

        exception(Exception.class, (e, req, res) -> {
            LOGGER.error(e.getMessage());
            LOGGER.error(e.getCause().getMessage());
            res.status(500);
            res.body(e.getMessage());
        });

        options("/*",
           (request, response) -> {
               String accessControlRequestHeaders = request
                       .headers("Access-Control-Request-Headers");
               if (accessControlRequestHeaders != null) {
                   response.header("Access-Control-Allow-Headers",
                           accessControlRequestHeaders);
               }
               String accessControlRequestMethod = request
                       .headers("Access-Control-Request-Method");
               if (accessControlRequestMethod != null) {
                   response.header("Access-Control-Allow-Methods",
                           accessControlRequestMethod);
               }
               return "OK";
           });
        before((request, response) -> response.header("Access-Control-Allow-Origin", "*"));
    }

    @Override
    public void run() {
    }

    @Override
    public void terminate() {
        stop();
    }
}
