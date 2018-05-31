package com.aegeanflow.rest;

import static spark.Spark.*;
import com.aegeanflow.core.AegeanFlow;
import com.aegeanflow.core.engine.DataFlowEngine;
import com.aegeanflow.core.engine.DataFlowEngineManager;
import com.aegeanflow.core.exception.NodeRuntimeException;
import com.aegeanflow.core.flow.Flow;
import com.aegeanflow.core.spi.AegeanFlowService;
import com.aegeanflow.core.workspace.Workspace;
import com.aegeanflow.rest.proxy.NodeErrorProxy;
import com.aegeanflow.rest.proxy.UUIDProxy;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.MediaType;
import java.util.List;

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

            //WORKSPACE
            get("/workspace/path", MediaType.APPLICATION_JSON, (req, res) -> {
                return objectMapper.createObjectNode().put("path", workspace.getPath());
            }, jsonTransformer);

            //WORKSPACE
            post("/workspace/path", MediaType.APPLICATION_JSON, (req, res) -> {
                JsonNode jsonNode = objectMapper.readTree(req.body());
                String path = jsonNode.get("path").asText();
                workspace.changePath(path);
                return objectMapper.createObjectNode().put("path", workspace.getPath());
            }, jsonTransformer);

            //NODE
            get("/node/list", MediaType.APPLICATION_JSON, (req, res) -> {
                res.type("application/json");
                return aegeanFlow.getNodeRepository().getNodeDefinitionList();
            }, jsonTransformer);

            //FLOW
            post("/flow",  MediaType.APPLICATION_JSON, (req, res) -> {
                Flow flow = objectMapper.readValue(req.body(), Flow.class);
                flow = workspace.save(flow);
                return new UUIDProxy(flow.getUuid());
            }, jsonTransformer);

            post("/flow/run",  (req, res) -> {
                Flow flow = new ObjectMapper().readValue(req.body(), Flow.class);
                DataFlowEngine de = flowManager.create(flow, true);
                List<Object> resultList = de.getResultList();
                for (Object result : resultList) {
                    System.out.println(result);
                }
                return new UUIDProxy(flow.getUuid());
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
