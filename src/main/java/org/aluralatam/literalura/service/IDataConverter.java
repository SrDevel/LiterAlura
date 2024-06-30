package org.aluralatam.literalura.service;

public interface IDataConverter {
    <T> T convert(String json, Class<T> clazz);
}
