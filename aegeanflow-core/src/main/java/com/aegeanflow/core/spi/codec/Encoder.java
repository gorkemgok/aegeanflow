package com.aegeanflow.core.spi.codec;

import java.io.IOException;
import java.io.InputStream;

public interface Encoder<T> {

    <I extends T> InputStream encode(I value) throws IOException;
}
