package com.aegeanflow.core.codec.json;

import com.aegeanflow.core.spi.codec.Codec;
import com.aegeanflow.core.spi.codec.Decoder;
import com.aegeanflow.core.spi.codec.Encoder;
import com.google.inject.Inject;

public class JsonCodec implements Codec<Object> {

    private final JsonEncoder jsonEncoder;

    private final JsonDecoder jsonDecoder;

    @Inject
    public JsonCodec(JsonEncoder jsonEncoder, JsonDecoder jsonDecoder) {
        this.jsonEncoder = jsonEncoder;
        this.jsonDecoder = jsonDecoder;
    }

    @Override
    public Encoder<Object> encoder() {
        return jsonEncoder;
    }

    @Override
    public Decoder<Object> decoder() {
        return jsonDecoder;
    }

    @Override
    public Class<Object> getCodecClass() {
        return Object.class;
    }
}
