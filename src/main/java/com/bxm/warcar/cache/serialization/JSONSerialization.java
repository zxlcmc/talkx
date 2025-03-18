package com.bxm.warcar.cache.serialization;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author allen
 * @date 2017-12-04
 */
public class JSONSerialization implements Serialization<String> {

    private static final Logger LOGGER = LoggerFactory.getLogger(JSONSerialization.class);

    @Override
    public String serialize(Object data) {
        if (null == data) {
            return null;
        }
        if (data instanceof String) {
            return data.toString();
        }
        return JSON.toJSONString(data);
    }

    @Override
    public <T> T deserialize(String modalValue, Class<T> cls) {
        if (cls.isAssignableFrom(String.class)) {
            return (T) modalValue;
        }
        return JSON.parseObject(modalValue, cls);
    }

    @Override
    public <T> List<T> deserializeList(String modalValue, Class<T> cls) {
        return JSON.parseArray(modalValue, cls);
    }

    @Override
    public <T> Map<String, T> deserializeMap(String modalValue, Class<T> cls) {
        Map<String, T> rst = Maps.newHashMap();
        Map<String, ? extends Object> map = JSONObject.parseObject(modalValue, Map.class);
        Set<? extends Map.Entry<String, ? extends Object>> entries = map.entrySet();
        for (Map.Entry<String, ? extends Object> entry : entries) {
            Object value = entry.getValue();
            if (value instanceof JSONObject) {
                T o = ((JSONObject) value).toJavaObject(cls);
                rst.put(entry.getKey(), o);
            }
            else {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("object {} is not instanceof JSONObject", value);
                }
            }
        }
        return rst;
    }
}
