package com.aegeanflow.core;

import com.aegeanflow.core.node.NodeModule;
import com.aegeanflow.core.node.NodeRepository;
import com.aegeanflow.core.spi.Node;
import com.aegeanflow.core.spi.annotation.NodeEntry;
import com.aegeanflow.core.example.ExampleModule;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.gorkemgok.annoconf.guice.InitSyncUtilModule;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

/**
 * Created by gorkem on 12.01.2018.
 */
public class CoreModule extends AbstractModule {
    private static final Logger LOGGER = LoggerFactory.getLogger(CoreModule.class);

    public static final String NODE_CLASSES = "nodeClasses";
    @Override
    protected void configure() {
        install(new NodeModule());
        install(new InitSyncUtilModule());
        bind(DataFlowEngineFactory.class).in(Singleton.class);
        bind(NodeRepository.class).in(Singleton.class);
        install(new ExampleModule());
        Reflections reflections = new Reflections();
        Set<Class<? extends Node>> nodeClasses = reflections.getSubTypesOf(Node.class);
        nodeClasses.stream()
                .filter(nodeClass -> nodeClass.isAnnotationPresent(NodeEntry.class))
                .forEach(nodeClass -> bind(nodeClass));
    }

    @Provides
    @Singleton
    @Named(NODE_CLASSES)
    Set<Class<? extends Node>> provideNodeClasses(){
        Reflections reflections = new Reflections();
        Set<Class<? extends Node>> nodeClasses = reflections.getSubTypesOf(Node.class);
        return nodeClasses;
    }
}
