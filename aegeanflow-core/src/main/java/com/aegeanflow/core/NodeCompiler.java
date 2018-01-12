package com.aegeanflow.core;

import com.aegeanflow.core.annotation.NodeInput;
import com.google.inject.Inject;

import java.lang.reflect.Method;
import java.util.Set;

public class NodeCompiler {

    private final Set<Node> nodeSet;

    @Inject
    public NodeCompiler(Set<Node> nodeSet) {
        this.nodeSet = nodeSet;
    }

    public void compile(Node node){

        for (Method method : node.getClass().getMethods()) {
            NodeInput nodeInput = method.getAnnotation(NodeInput.class);
            if (nodeInput != null) {

            }
        }
    }
}
