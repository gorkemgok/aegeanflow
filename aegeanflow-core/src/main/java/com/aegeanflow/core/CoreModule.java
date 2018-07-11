package com.aegeanflow.core;

import com.aegeanflow.core.engine.DataFlowEngineManager;
import com.aegeanflow.core.spi.RunnableNode;
import com.aegeanflow.core.spi.annotation.NodeEntry;
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
        install(new InitSyncUtilModule());
        bind(DataFlowEngineManager.class).in(Singleton.class);
        bind(NodeRepository.class).in(Singleton.class);
        Reflections reflections = new Reflections("com.aegeanflow");
        Set<Class<? extends RunnableNode>> nodeClasses = reflections.getSubTypesOf(RunnableNode.class);
        nodeClasses.stream()
                .filter(nodeClass -> nodeClass.isAnnotationPresent(NodeEntry.class))
                .forEach(nodeClass -> bind(nodeClass));
    }

    @Provides
    @Singleton
    @Named(NODE_CLASSES)
    Set<Class<? extends RunnableNode>> provideNodeClasses(){
        Reflections reflections = new Reflections("com.aegeanflow");
        Set<Class<? extends RunnableNode>> nodeClasses = reflections.getSubTypesOf(RunnableNode.class);
        return nodeClasses;
    }
}
