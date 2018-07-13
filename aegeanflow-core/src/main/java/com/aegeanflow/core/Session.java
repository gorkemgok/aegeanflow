package com.aegeanflow.core;

import com.aegeanflow.core.exception.IllegalNodeConfigurationException;
import com.aegeanflow.core.spi.AnnotatedBox;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.name.Named;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ExecutorService;

public class Session {

    private static final Logger LOGGER = LoggerFactory.getLogger(Session.class);

    private final Router router;

    private final BoxRepository boxRepository;

    private final Injector injector;

    private final List<Node> nodeList;

    private final UUID uuid;

    private final ExecutorService executor;

    @Inject
    public Session(Router router, BoxRepository boxRepository, Injector injector,
                   @Named(CoreModule.MAIN_EXECUTOR_SERVICE) ExecutorService executor) {
        this.router = router;
        this.boxRepository = boxRepository;
        this.injector = injector;
        this.executor = executor;
        this.nodeList = new ArrayList<>();
        this.uuid = UUID.randomUUID();
    }

    public UUID getUuid() {
        return uuid;
    }

    public void run() {
        //nodeList.forEach(node -> executor.submit(() -> node.execute()));
        nodeList.forEach(node -> node.executeIfSatisfied());
    }

    public <T> void setInput(UUID uuid, Input<T> input, T value){
        Node node =
            nodeList.stream().filter(_node -> _node.is(uuid)).findAny().orElseThrow(() -> new NoSuchElementException());
        node.accept(input, value);
    }

    public AnnotatedNode newBox(UUID uuid, Class<? extends AnnotatedBox> clazz) throws IllegalNodeConfigurationException {
        AnnotatedBox annotatedBox = injector.getInstance(clazz);
        annotatedBox.setUUID(uuid);
        AnnotatedNode annotatedNode = new AnnotatedNode();
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

    public void connect(UUID sourceUUID, String outputName, UUID targetUUID, String inputName) {
        Optional<Node> sourceNodeOptional = nodeList.stream().filter(node -> node.getUUID().equals(sourceUUID)).findFirst();
        if (sourceNodeOptional.isPresent()) {
            Optional<Node> targetNodeOptional =
                nodeList.stream().filter(node -> node.getUUID().equals(targetUUID)).findFirst();
            if (targetNodeOptional.isPresent()) {
                Node sourceNode = sourceNodeOptional.get();
                Node targetNode = targetNodeOptional.get();

                Optional<Output<?>> outputOptional = sourceNode.getOutput(outputName);
                if (outputOptional.isPresent()) {
                    Optional<Input<?>> inputOptional = targetNode.getInput(inputName);
                    if (inputOptional.isPresent()) {
                        Output output = outputOptional.get();
                        Input input = inputOptional.get();
                        router.connect(sourceNode, output, targetNode, input);
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
}
