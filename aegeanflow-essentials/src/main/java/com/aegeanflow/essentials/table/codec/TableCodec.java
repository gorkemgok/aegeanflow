package com.aegeanflow.essentials.table.codec;

import com.aegeanflow.core.spi.codec.Codec;
import com.aegeanflow.core.spi.codec.Decoder;
import com.aegeanflow.core.spi.codec.Encoder;
import com.aegeanflow.essentials.table.Table;
import com.google.inject.Inject;

public class TableCodec implements Codec<Table> {

    private final TableDecoder decoder;

    private final TableEncoder encoder;

    @Inject
    public TableCodec(TableDecoder decoder, TableEncoder encoder) {
        this.decoder = decoder;
        this.encoder = encoder;
    }

    @Override
    public Encoder<Table> encoder() {
        return encoder;
    }

    @Override
    public Decoder<Table> decoder() {
        return decoder;
    }

    @Override
    public Class<Table> getCodecClass() {
        return Table.class;
    }
}
