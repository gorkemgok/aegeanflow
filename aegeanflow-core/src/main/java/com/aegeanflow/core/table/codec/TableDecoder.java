package com.aegeanflow.core.table.codec;

import com.aegeanflow.core.spi.codec.Decoder;
import com.aegeanflow.core.table.Table;

import java.io.IOException;
import java.io.InputStream;

public class TableDecoder implements Decoder<Table> {
    @Override
    public <I extends Table> I decoder(InputStream inputStream, Class<I> clazz) throws IOException {
        return null;
    }
}
