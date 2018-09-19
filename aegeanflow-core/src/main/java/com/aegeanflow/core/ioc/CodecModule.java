package com.aegeanflow.core.ioc;

import com.aegeanflow.core.spi.codec.Codec;
import com.aegeanflow.core.codec.json.JsonCodec;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Scopes;
import com.google.inject.Singleton;
import com.google.inject.multibindings.Multibinder;

public class CodecModule extends AbstractModule {
    @Override
    protected void configure() {
        Multibinder<Codec> codecProviderMultibinder = Multibinder.newSetBinder(binder(), Codec.class);
        codecProviderMultibinder.addBinding().to(JsonCodec.class).in(Scopes.SINGLETON);
        bind(JsonCodec.class).in(Scopes.SINGLETON);
    }

    @Provides
    @Singleton
    @Default
    Codec<Object> provideDefaultCodec(JsonCodec jsonCodec) {
        return jsonCodec;
    }
}
