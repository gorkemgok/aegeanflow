package com.aegeanflow.core.codec.json;

import com.aegeanflow.core.spi.codec.Decoder;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;

public class JsonDecoder implements Decoder<Object> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public <I> I decoder(InputStream inputStream, Class<I> clazz) throws IOException {
        return objectMapper.readValue(inputStream, clazz);
    }
}
