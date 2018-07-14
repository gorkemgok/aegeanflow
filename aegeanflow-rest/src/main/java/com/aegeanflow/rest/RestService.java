package com.aegeanflow.rest;

import static spark.Spark.*;
import com.aegeanflow.core.AegeanFlow;
import com.aegeanflow.core.exception.NodeRuntimeException;
import com.aegeanflow.core.node.NodeRepository;
import com.aegeanflow.core.model.SessionModel;
import com.aegeanflow.core.session.Session;
import com.aegeanflow.core.session.SessionBuilder;
import com.aegeanflow.core.spi.AegeanFlowService;
import com.aegeanflow.core.workspace.Workspace;
import com.aegeanflow.rest.proxy.NodeErrorProxy;
import com.aegeanflow.rest.proxy.UUIDProxy;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    private final ObjectMapper objectMapper;

    private final SessionBuilder sessionBuilder;

    private final NodeRepository nodeRepository;

    @Inject
    public RestService(RestConfig restConfig,
                       AegeanFlow aegeanFlow,
                       JsonTransformer jsonTransformer,
                       Workspace workspace,
                       SessionBuilder sessionBuilder, NodeRepository nodeRepository) {
        this.restConfig = restConfig;
        this.aegeanFlow = aegeanFlow;
        this.jsonTransformer = jsonTransformer;
        this.workspace = workspace;
        this.sessionBuilder = sessionBuilder;
        this.nodeRepository = nodeRepository;
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
                return nodeRepository.getNodeDefinitions();
            }, jsonTransformer);

            //FLOW
            post("/model",  MediaType.APPLICATION_JSON, (req, res) -> {
                SessionModel sessionModel = objectMapper.readValue(req.body(), SessionModel.class);
                workspace.save(sessionModel);
                return "ok";
            }, jsonTransformer);

            post("/session",  (req, res) -> {
                SessionModel sessionModel = new ObjectMapper().readValue(req.body(), SessionModel.class);
                Session session = sessionBuilder.buildFrom(sessionModel);
                return new UUIDProxy(session.getUuid());
            });

            get("/model/list",  (req, res) -> {
                List<SessionModel> sessionModelList = workspace.getFlowList();
                return sessionModelList;
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
            LOGGER.error(e.getMessage(), e);
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

        LOGGER.info("Rest service started");
    }

    @Override
    public void run() {
    }

    @Override
    public void terminate() {
        stop();
    }
}
