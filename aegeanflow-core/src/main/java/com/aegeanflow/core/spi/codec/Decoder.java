package com.aegeanflow.core.spi.codec;

import java.io.IOException;
import java.io.InputStream;

public interface Decoder<T> {

    <I extends T> I decoder(InputStream inputStream, Class<I> clazz) throws IOException;
}
