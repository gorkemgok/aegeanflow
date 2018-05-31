package com.aegeanflow.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import spark.ResponseTransformer;

public class JsonTransformer implements ResponseTransformer {

    private final ObjectMapper objectMapper;

    @Inject
    public JsonTransformer(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public String render(Object model) throws JsonProcessingException {
        if (model instanceof JsonNode) {
            return ((JsonNode) model).toString();
        }
        return objectMapper.writeValueAsString(model);
    }

}
