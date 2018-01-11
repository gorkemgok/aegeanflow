package com.aegeanflow.core;

import com.aegeanflow.core.annotation.NodeInput;

import java.lang.reflect.Method;

public class NodeCompiler {

    public void compile(Node node){
        for (Method method : node.getClass().getMethods()) {
            NodeInput nodeInput = method.getAnnotation(NodeInput.class);
            if (nodeInput != null) {
                System.out.println(method.getName());
            }
        }
    }
}
