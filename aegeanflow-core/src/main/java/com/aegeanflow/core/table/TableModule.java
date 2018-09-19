package com.aegeanflow.core.table;

import com.aegeanflow.core.spi.codec.Codec;
import com.aegeanflow.core.table.codec.TableCodec;
import com.aegeanflow.core.table.codec.TableDecoder;
import com.aegeanflow.core.table.codec.TableEncoder;
import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.google.inject.multibindings.Multibinder;

public class TableModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(TableEncoder.class).in(Scopes.SINGLETON);
        bind(TableDecoder.class).in(Scopes.SINGLETON);
        Multibinder<Codec> codecMultibinder = Multibinder.newSetBinder(binder(), Codec.class);
        codecMultibinder.addBinding().to(TableCodec.class);
    }
}
