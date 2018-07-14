package com.aegeanflow.core.spi;

import com.aegeanflow.core.spi.box.AnnotatedBox;
import com.aegeanflow.core.spi.node.Node;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

public interface Plugin {

    String name();

    default Map<String, Class<? extends Node>> provideNodeClasses(){
        return Collections.emptyMap();
    }

    default Collection<Class<? extends AnnotatedBox<?>>> provideAnnotatedBoxes(){
        return Collections.emptyList();
    }
}
