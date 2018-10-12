package com.aegeanflow.core.session;

import com.aegeanflow.core.concurrent.ThreadManager;
import com.aegeanflow.core.box.BoxRepository;
import com.aegeanflow.core.exception.IllegalNodeConfigurationException;
import com.aegeanflow.core.logger.SessionLogManager;
import com.aegeanflow.core.logger.SessionLogManagerFactory;
import com.aegeanflow.core.node.AnnotatedNode;
import com.aegeanflow.core.route.RouteOptions;
import com.aegeanflow.core.route.RouterFactory;
import com.aegeanflow.core.spi.box.AnnotatedBox;
import com.aegeanflow.core.spi.node.Node;
import com.aegeanflow.core.spi.parameter.Input;
import com.aegeanflow.core.spi.parameter.Output;
import com.aegeanflow.core.route.Router;
import com.google.inject.Inject;
import com.google.inject.Injector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class Session {

    private static final Logger LOGGER = LoggerFactory.getLogger(Session.class);

    private final Router router;

    private final BoxRepository boxRepository;

    private final Injector injector;

    private final List<Node> nodeList;

    private final UUID uuid;

    private final ThreadManager threadManager;

    private final SessionLogManager logManager;

    private final SessionContext context;

    @Inject
    public Session(Injector mainInjector,
                   BoxRepository boxRepository,
                   RouterFactory routerFactory,
                   SessionLogManagerFactory logManagerFactory,
                   ThreadManager threadManager) {
        this.boxRepository = boxRepository;
        this.router = routerFactory.create(this);
        this.threadManager = threadManager;
        this.nodeList = new ArrayList<>();
        this.uuid = UUID.randomUUID();
        this.logManager = logManagerFactory.create(this);
        this.injector = mainInjector.createChildInjector(new SessionModule(this, logManager));
        this.context = this.injector.getInstance(SessionContext.class);
    }

    public UUID getId() {
        return uuid;
    }

    public void awaitCompletion() {
        threadManager.awaitCompletion(this);
    }

    public void run() {
        nodeList.forEach(node -> {
            if (node.getState() == Node.State.WAITING && node.isReady() && router.isPrecedencesDone(node)) {
                threadManager.run(this, node);
            }
        });
    }

    public <T> void setInput(UUID uuid, Input<T> input, T value){
        Node node =
            nodeList.stream().filter(_node -> _node.is(uuid)).findAny().orElseThrow(() -> new NoSuchElementException());
        node.accept(input, value);
    }

    public AnnotatedNode newBox(UUID uuid, Class<? extends AnnotatedBox> clazz) throws IllegalNodeConfigurationException {
        AnnotatedBox annotatedBox = injector.getInstance(clazz);
        annotatedBox.setUUID(uuid);
        AnnotatedNode annotatedNode = new AnnotatedNode(boxRepository);
        annotatedNode.initialize(annotatedBox, router);
        nodeList.add(annotatedNode);
        return annotatedNode;
    }

    public <N extends Node> N newNode(UUID uuid, Class<? extends N> clazz) {
        N node = injector.getInstance(clazz);
        node.initialize(uuid, router);
        nodeList.add(node);
        return node;
    }

    public void connect(UUID sourceUUID, String outputName, UUID targetUUID, String inputName, RouteOptions routeOptions) {
        Optional<Node> sourceNodeOptional = nodeList.stream().filter(node -> node.getId().equals(sourceUUID)).findFirst();
        if (sourceNodeOptional.isPresent()) {
            Optional<Node> targetNodeOptional =
                nodeList.stream().filter(node -> node.getId().equals(targetUUID)).findFirst();
            if (targetNodeOptional.isPresent()) {
                Node sourceNode = sourceNodeOptional.get();
                Node targetNode = targetNodeOptional.get();

                Optional<Output<?>> outputOptional = sourceNode.getOutput(outputName);
                if (outputOptional.isPresent()) {
                    Optional<Input<?>> inputOptional = targetNode.getInput(inputName);
                    if (inputOptional.isPresent()) {
                        Output output = outputOptional.get();
                        Input input = inputOptional.get();
                        router.connect(sourceNode, output, targetNode, input, routeOptions);
                    } else {
                        //TODO: log
                        LOGGER.warn("Cant find input {} of {}", inputName, targetUUID);
                    }
                } else {
                    //TODO: log
                    LOGGER.warn("Cant find output {} of {}", outputName, sourceUUID);
                }
            } else {
                //TODO: log
                LOGGER.warn("Cant find target node: {}", targetUUID);
            }
        } else {
            //TODO: log
            LOGGER.warn("Cant find source node: {}", sourceUUID);
        }
    }

    public Router getRouter() {
        return router;
    }

    public SessionContext getContext() {
        return context;
    }
}
