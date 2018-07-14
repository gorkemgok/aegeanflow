package com.aegeanflow.core.node;

import com.aegeanflow.core.spi.box.AnnotatedBox;
import com.aegeanflow.core.spi.node.Node;
import com.gorkemgok.annoconf.guice.Initiable;

public class NodeDefinition implements Initiable{

    private final Class<? extends Node> nodeClass;

    private final Class<? extends AnnotatedBox> boxClass;

    private final String name;

    public NodeDefinition( String name, Class<? extends Node> nodeClass) {
        this(name, nodeClass, null);
    }

    public NodeDefinition( String name, Class<? extends Node> nodeClass, Class<? extends AnnotatedBox> boxClass) {
        this.nodeClass = nodeClass;
        this.boxClass = boxClass;
        this.name = name;
    }

    public Class<? extends Node> getNodeClass() {
        return nodeClass;
    }

    public Class<? extends AnnotatedBox> getBoxClass() {
        return boxClass;
    }

    public String getName() {
        return name;
    }

    @Override
    public void init() {

    }
}
