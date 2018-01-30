package com.aegeanflow.core.node;

import com.aegeanflow.core.CoreModule;
import com.aegeanflow.core.definition.NodeDefinition;
import com.aegeanflow.core.exception.IllegalNodeConfigurationException;
import com.aegeanflow.core.spi.RunnableNode;
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

    private final List<NodeInfo> nodeInfoList;

    private final Set<Class<? extends RunnableNode>> nodeClasses;

    @Inject
    public NodeRepository(@Named(CoreModule.NODE_CLASSES) Set<Class<? extends RunnableNode>> nodeClasses) {
        this.nodeClasses = nodeClasses;
        nodeInfoList = new ArrayList<>();
    }

    public void register(Class<? extends RunnableNode> nodeClass) throws IllegalNodeConfigurationException {
        NodeInfo nodeInfo = CompilerUtil.compile(nodeClass);
        nodeInfoList.add(nodeInfo);
    }

    public List<NodeDefinition> getNodeDefinitionList(){
        return nodeInfoList.stream()
                .map(nodeInfo -> nodeInfo.getDefinition())
                .collect(Collectors.toList());
    }

    public List<NodeInfo> getNodeInfoList() {
        return Collections.unmodifiableList(nodeInfoList);
    }

    @Override
    public void init() {
        nodeClasses.forEach(nodeClass -> {
            try {
                register(nodeClass);
                LOGGER.info("Registered {}", nodeClass.getName());
            } catch (IllegalNodeConfigurationException e) {
                LOGGER.error("Cant register runnableNode {}. {}", nodeClass.getName(), e.getMessage());
            }
        });
    }
}
