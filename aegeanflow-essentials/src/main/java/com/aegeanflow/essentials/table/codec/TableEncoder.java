package com.aegeanflow.essentials.table.codec;

import com.aegeanflow.core.spi.codec.Encoder;
import com.aegeanflow.essentials.table.Table;

import java.io.IOException;
import java.io.InputStream;

public class TableEncoder implements Encoder<Table> {
    @Override
    public <I extends Table> InputStream encode(I value) throws IOException {
        return null;
    }
}
