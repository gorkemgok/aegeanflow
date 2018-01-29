package com.aegeanflow.application;

import com.aegeanflow.core.AegeanFlow;
import com.aegeanflow.core.CoreModule;
import com.aegeanflow.core.spi.AegeanFlowService;
import com.google.inject.*;
import com.google.inject.multibindings.Multibinder;
import com.gorkemgok.annoconf.*;
import com.gorkemgok.annoconf.guice.AnnoConfModule;
import com.gorkemgok.annoconf.guice.InitSyncUtilModule;
import com.gorkemgok.annoconf.source.impl.PropertyFileSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Created by gorkem on 15.01.2018.
 */
public class BootstrapModule extends AbstractModule {

    private static final Logger LOGGER = LoggerFactory.getLogger(BootstrapModule.class);

    @Override
    protected void configure() {
        Multibinder<AegeanFlowService> serviceMultibinder = Multibinder.newSetBinder(binder(), AegeanFlowService.class);
        install(new InitSyncUtilModule());
        install(new CoreModule());

        ConfigOptions configOptions = null;
        AnnoConfModule annoConfModule = null;
        try {
            configOptions = new ConfigOptions()
                    .addSource(
                            new PropertyFileSource(
                                    this.getClass().getClassLoader().getResource("aegeanflow.properties").getFile()))
                    .addScanPackage("com.aegeanflow");
            annoConfModule = new AnnoConfModule(configOptions);
            install(annoConfModule);
        } catch (IOException e) {
            LOGGER.error("Cant find property file. {}", e.getMessage());
            return;
        }
        Config config = annoConfModule.unwrapConfig();
        ServiceManager serviceManager = new ServiceManager(config);
        try {
            for (Service service : serviceManager.getServiceSet()){
                if (service.isLoadable()){
                    Object serviceInstance = service.getInstance();
                    if (serviceInstance instanceof Module){
                        install((Module) serviceInstance);
                    }
                }
            }
        } catch (CrossDependencyException e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    @Provides
    @Singleton
    AegeanFlow provideAegeanFlow(Injector injector) {
        return AegeanFlow.start(injector);
    }
}
