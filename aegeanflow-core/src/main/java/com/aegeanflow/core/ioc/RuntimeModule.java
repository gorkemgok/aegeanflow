package com.aegeanflow.core.ioc;

import com.aegeanflow.core.concurrent.ThreadManager;
import com.aegeanflow.core.box.BoxRepository;
import com.aegeanflow.core.box.processor.BoxAnnotationProcessor;
import com.aegeanflow.core.node.NodeRepository;
import com.aegeanflow.core.session.SessionBuilder;
import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.google.inject.name.Names;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RuntimeModule extends AbstractModule {

    public static final String MAIN_EXECUTOR_SERVICE = "mainExecutorService";

    @Override
    protected void configure() {
        bind(BoxRepository.class).in(Scopes.SINGLETON);
        bind(ExecutorService.class).annotatedWith(Names.named(MAIN_EXECUTOR_SERVICE))
            .toInstance(Executors.newFixedThreadPool(4));
        bind(NodeRepository.class).in(Scopes.SINGLETON);
        bind(BoxAnnotationProcessor.class).in(Scopes.SINGLETON);
        bind(SessionBuilder.class).in(Scopes.SINGLETON);
        bind(ThreadManager.class).in(Scopes.SINGLETON);
    }
}
