package com.aegeanflow.core;

import com.aegeanflow.core.spi.annotation.NodeEntry;
import com.aegeanflow.core.definition.CompilerUtil;
import com.aegeanflow.core.example.ExampleModule;
import com.aegeanflow.core.exception.IllegalNodeConfigurationException;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by gorkem on 12.01.2018.
 */
public class CoreModule extends AbstractModule {
    private static final Logger LOGGER = LoggerFactory.getLogger(CoreModule.class);
    @Override
    protected void configure() {
        bind(DataFlowEngineFactory.class).in(Singleton.class);
        install(new ExampleModule());
        Reflections reflections = new Reflections();
        Set<Class<? extends Node>> nodeClasses = reflections.getSubTypesOf(Node.class);
        nodeClasses.stream()
                .filter(nodeClass -> nodeClass.isAnnotationPresent(NodeEntry.class))
                .forEach(nodeClass -> bind(nodeClass));
    }

    @Provides
    @Singleton
    @Named("nodeClasses")
    Set<Class<? extends Node>> provideNodeClasses(){
        Reflections reflections = new Reflections();
        Set<Class<? extends Node>> nodeClasses = reflections.getSubTypesOf(Node.class);
        return nodeClasses;
    }

    @Provides
    @Singleton
    List<CompiledNodeInfo> provideCompiledNodeInfo(@Named("nodeClasses") Set<Class<? extends Node>> nodeClasses){
        List<CompiledNodeInfo> compiledNodeInfoList = new ArrayList<>();
        for (Class nodeClass : nodeClasses){
            try {
                CompiledNodeInfo compiledNodeInfo = CompilerUtil.compile(nodeClass);
                compiledNodeInfoList.add(compiledNodeInfo);
            } catch (IllegalNodeConfigurationException e) {
                //TODO: log
                LOGGER.info(e.getMessage());
            }
        }
        return compiledNodeInfoList;
    }
}
