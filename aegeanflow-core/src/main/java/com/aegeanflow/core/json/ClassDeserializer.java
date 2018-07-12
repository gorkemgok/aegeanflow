package com.aegeanflow.core.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;

/**
 * Created by gorkem on 22.01.2018.
 */
public class ClassDeserializer extends StdDeserializer<Class> {
    protected ClassDeserializer(Class<?> vc) {
        super(vc);
    }

    public ClassDeserializer() {
        this(null);
    }

    @Override
    public Class deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        String className = jsonParser.getText();
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new IOException(e);
        }
    }
}
