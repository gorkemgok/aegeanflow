package com.aegeanflow.core.node;

import com.aegeanflow.core.CompiledNodeInfo;
import com.aegeanflow.core.CoreModule;
import com.aegeanflow.core.definition.NodeDefinition;
import com.aegeanflow.core.exception.IllegalNodeConfigurationException;
import com.aegeanflow.core.spi.Node;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.gorkemgok.annoconf.guice.Initiable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by gorkem on 18.01.2018.
 */
public class NodeRepository implements Initiable {

    private static final Logger LOGGER = LoggerFactory.getLogger(NodeRepository.class);

    private final List<CompiledNodeInfo> compiledNodeInfoList;

    private final Set<Class<? extends Node>> nodeClasses;

    @Inject
    public NodeRepository(@Named(CoreModule.NODE_CLASSES) Set<Class<? extends Node>> nodeClasses) {
        this.nodeClasses = nodeClasses;
        compiledNodeInfoList = new ArrayList<>();
    }

    public void register(Class<? extends Node> nodeClass) throws IllegalNodeConfigurationException {
        CompiledNodeInfo compiledNodeInfo = CompilerUtil.compile(nodeClass);
        compiledNodeInfoList.add(compiledNodeInfo);
    }

    public List<NodeDefinition> getNodeDefinitionList(){
        return compiledNodeInfoList.stream()
                .map(compiledNodeInfo -> compiledNodeInfo.getDefinition())
                .collect(Collectors.toList());
    }

    public List<CompiledNodeInfo> getCompiledNodeInfoList() {
        return Collections.unmodifiableList(compiledNodeInfoList);
    }

    @Override
    public void init() {
        nodeClasses.forEach(nodeClass -> {
            try {
                register(nodeClass);
                LOGGER.info("Registered {}", nodeClass.getName());
            } catch (IllegalNodeConfigurationException e) {
                LOGGER.error("Cant register node {}. {}", nodeClass.getName(), e.getMessage());
            }
        });
    }
}
