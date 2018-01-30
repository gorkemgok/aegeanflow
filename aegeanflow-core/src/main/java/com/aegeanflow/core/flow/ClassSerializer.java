package com.aegeanflow.core.flow;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

/**
 * Created by gorkem on 22.01.2018.
 */
public class ClassSerializer extends StdSerializer<Class>{

    protected ClassSerializer(Class<Class> t) {
        super(t);
    }

    public ClassSerializer() {
        this(null);
    }

    @Override
    public void serialize(Class aClass, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeString(aClass.getTypeName());
    }
}
