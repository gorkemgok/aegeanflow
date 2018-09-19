package com.aegeanflow.core.spi.codec;

public interface Codec<T> {

    Encoder<T> encoder();

    Decoder<T> decoder();

    Class<T> getCodecClass();
}
