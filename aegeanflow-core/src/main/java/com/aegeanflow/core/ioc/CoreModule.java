package com.aegeanflow.core.ioc;

import com.aegeanflow.core.box.processor.BoxAnnotationProcessor;
import com.aegeanflow.core.node.NodeRepository;
import com.aegeanflow.core.plugin.CorePlugin;
import com.aegeanflow.core.box.BoxRepository;
import com.aegeanflow.core.session.SessionBuilder;
import com.aegeanflow.core.spi.box.AnnotatedBox;
import com.aegeanflow.core.spi.Plugin;
import com.aegeanflow.core.spi.annotation.NodeEntry;
import com.google.inject.AbstractModule;
import com.google.inject.Module;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.multibindings.Multibinder;
import com.google.inject.name.Named;
import com.google.inject.name.Names;
import com.gorkemgok.annoconf.ConfigOptions;
import com.gorkemgok.annoconf.guice.AnnoConfModule;
import com.gorkemgok.annoconf.guice.InitSyncUtilModule;
import com.gorkemgok.annoconf.source.impl.PropertyFileSource;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
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
        bind(NodeRepository.class).in(Singleton.class);
        bind(ExecutorService.class).annotatedWith(Names.named(MAIN_EXECUTOR_SERVICE))
            .toInstance(Executors.newFixedThreadPool(4));
        bind(BoxAnnotationProcessor.class).in(Singleton.class);
        bind(SessionBuilder.class).in(Singleton.class);

//        //ANNOCONF
//        ConfigOptions configOptions = ConfigOptions.withSystemPropertySource();
//        try {
//            configOptions.addSource(new PropertyFileSource("aegeanflow.properties"));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        AnnoConfModule annoConfModule = new AnnoConfModule(configOptions);
//        annoConfModule.unwrapConfig().getServices()
//            .forEach(service -> {
//                if (service.isLoadable()){
//                    install((Module) service.getInstance());
//                }
//            });
    }
}
