package com.aegeanflow.core.ioc;

import com.aegeanflow.core.plugin.CorePlugin;
import com.aegeanflow.core.session.Session;
import com.aegeanflow.core.session.SessionFactory;
import com.aegeanflow.core.session.SessionModule;
import com.aegeanflow.core.spi.Plugin;
import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.google.inject.multibindings.Multibinder;
import com.gorkemgok.annoconf.guice.InitSyncUtilModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by gorkem on 12.01.2018.
 */
public class CoreModule extends AbstractModule {
    private static final Logger LOGGER = LoggerFactory.getLogger(CoreModule.class);

    @Override
    protected void configure() {
        Multibinder<Plugin> pluginMultibinder = Multibinder.newSetBinder(binder(), Plugin.class);
        pluginMultibinder.addBinding().to(CorePlugin.class);

        bind(SessionFactory.class).in(Scopes.SINGLETON);
        install(new InitSyncUtilModule());
        install(new RuntimeModule());
        install(new CodecModule());
        install(new RouterModule());
        install(new TunnelModule());
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
