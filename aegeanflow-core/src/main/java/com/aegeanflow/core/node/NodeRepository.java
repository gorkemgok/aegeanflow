package com.aegeanflow.core.node;

import com.aegeanflow.core.box.BoxRepository;
import com.aegeanflow.core.spi.Plugin;
import com.aegeanflow.core.spi.node.Node;
import com.google.inject.Inject;
import com.gorkemgok.annoconf.guice.Initiable;

import java.util.*;

public class NodeRepository implements Initiable {

    private final Collection<NodeDefinition> nodeDefinitions;

    private final BoxRepository boxRepository;

    private final Set<Plugin> plugins;

    @Inject
    public NodeRepository(BoxRepository boxRepository, Set<Plugin> plugins) {
        this.nodeDefinitions = new ArrayList<>();
        this.boxRepository = boxRepository;
        this.plugins = plugins;
    }

    public Collection<NodeDefinition> getNodeDefinitions() {
        return Collections.unmodifiableCollection(nodeDefinitions);
    }

    public void register(NodeDefinition nodeDefinition) {
        nodeDefinitions.add(nodeDefinition);
    }

    @Override
    public void init() {
        plugins.forEach(plugin -> {
            plugin.provideNodeClasses().forEach((key, value) -> register(new NodeDefinition(key, value)));

            boxRepository.getBoxInfoList().forEach(boxInfo ->
                register(
                    new NodeDefinition(boxInfo.getDefinition().getName(), AnnotatedNode.class, boxInfo.getNodeClass()))
            );
        });
    }
}