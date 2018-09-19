package com.aegeanflow.core.codec;

import com.aegeanflow.core.ioc.Default;
import com.aegeanflow.core.spi.codec.Codec;
import com.google.inject.Inject;

import java.util.Set;

public class CodecProvider {

    private final Set<Codec> codecs;

    private final Codec<Object> defaultCodec;

    @Inject
    public CodecProvider(Set<Codec> codecs, @Default Codec<Object> defaultCodec) {
        this.codecs = codecs;
        this.defaultCodec = defaultCodec;
    }

    public <T> Codec<T> getCodec(Class<T> codecClass) {
        return codecs.stream()
            .filter(codec -> codec.getCodecClass().equals(codecClass))
            .findAny()
            .orElse(defaultCodec);
    }
}
