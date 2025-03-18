package com.bxm.warcar.cache.serialization;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Map;

/**
 * @author allen
 * @date 2017-12-04
 */
public class ByteSerialization implements Serialization<byte[]> {

    @Override
    public byte[] serialize(Object data) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        return baos.toByteArray();
    }

    @Override
    public <T> T deserialize(byte[] modalValue, Class<T> cls) {
        return null;
    }

    @Override
    public <T> Map<String, T> deserializeMap(byte[] modalValue, Class<T> cls) {
        return null;
    }

    @Override
    public <T> List<T> deserializeList(byte[] modalValue, Class<T> cls) {
        return null;
    }
}
