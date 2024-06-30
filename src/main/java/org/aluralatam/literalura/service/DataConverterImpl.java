package org.aluralatam.literalura.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class DataConverterImpl implements IDataConverter{
    private final ObjectMapper mapper = new ObjectMapper();
    @Override
    public <T> T convert(String json, Class<T> clazz) {
        try {
            return mapper.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
