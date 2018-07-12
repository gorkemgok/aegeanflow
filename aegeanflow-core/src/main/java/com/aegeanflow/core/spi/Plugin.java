package com.aegeanflow.core.spi;

import com.aegeanflow.core.Node;

import java.util.Collection;
import java.util.Collections;

public interface Plugin {

    String name();

    default Collection<Class<? extends Node>> provideNodeClasses(){
        return Collections.emptyList();
    }

    default Collection<Class<? extends AnnotatedBox<?>>> provideAnnotatedBoxes(){
        return Collections.emptyList();
    }
}
