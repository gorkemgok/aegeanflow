package com.aegeanflow.core.codec.json;

import com.aegeanflow.core.spi.codec.Encoder;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class JsonEncoder implements Encoder<Object> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public <I> InputStream encode(I value) throws IOException {
        return new ByteArrayInputStream(objectMapper.writeValueAsBytes(value));
    }
}
