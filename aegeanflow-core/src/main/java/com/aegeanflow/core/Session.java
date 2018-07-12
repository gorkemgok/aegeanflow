package com.aegeanflow.core;

import com.aegeanflow.core.exception.IllegalNodeConfigurationException;
import com.aegeanflow.core.spi.AnnotatedBox;
import com.google.inject.Inject;
import com.google.inject.Injector;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Session {

    private final Router router;

    private final BoxRepository boxRepository;

    private final Injector injector;

    private final List<Node> nodeList;

    private final UUID uuid;

    @Inject
    public Session(Router router, BoxRepository boxRepository, Injector injector) {
        this.router = router;
        this.boxRepository = boxRepository;
        this.injector = injector;
        this.nodeList = new ArrayList<>();
        this.uuid = UUID.randomUUID();
    }

    public UUID getUuid() {
        return uuid;
    }

    public <T> AnnotatedNode<T> newBox(UUID uuid, Class<? extends AnnotatedBox<T>> clazz) throws IllegalNodeConfigurationException {
        AnnotatedBox<T> annotatedBox = injector.getInstance(clazz);
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

    public Router getRouter() {
        return router;
    }
}
