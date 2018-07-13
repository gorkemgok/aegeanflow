package com.aegeanflow.core;

import com.aegeanflow.core.spi.AnnotatedBox;
import com.aegeanflow.core.spi.Plugin;
import com.aegeanflow.core.spi.annotation.NodeEntry;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.multibindings.Multibinder;
import com.google.inject.name.Named;
import com.google.inject.name.Names;
import com.gorkemgok.annoconf.guice.InitSyncUtilModule;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by gorkem on 12.01.2018.
 */
public class CoreModule extends AbstractModule {
    private static final Logger LOGGER = LoggerFactory.getLogger(CoreModule.class);

    public static final String NODE_CLASSES = "nodeClasses";

    public static final String MAIN_EXECUTOR_SERVICE = "mainExecutorService";
    @Override
    protected void configure() {
        Multibinder<Plugin> pluginMultibinder = Multibinder.newSetBinder(binder(), Plugin.class);
        pluginMultibinder.addBinding().to(CorePlugin.class);

        install(new InitSyncUtilModule());
        bind(BoxRepository.class).in(Singleton.class);
        Reflections reflections = new Reflections("com.aegeanflow");
        Set<Class<? extends AnnotatedBox>> nodeClasses = reflections.getSubTypesOf(AnnotatedBox.class);
        nodeClasses.stream()
                .filter(nodeClass -> nodeClass.isAnnotationPresent(NodeEntry.class))
                .forEach(nodeClass -> bind(nodeClass));
        bind(ExecutorService.class).annotatedWith(Names.named(MAIN_EXECUTOR_SERVICE))
            .toInstance(Executors.newFixedThreadPool(4));

    }

    @Provides
    @Singleton
    @Named(NODE_CLASSES)
    Set<Class<? extends AnnotatedBox>> provideNodeClasses(){
        Reflections reflections = new Reflections("com.aegeanflow");
        Set<Class<? extends AnnotatedBox>> nodeClasses = reflections.getSubTypesOf(AnnotatedBox.class);
        return nodeClasses;
    }
}
