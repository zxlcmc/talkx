package com.bxm.warcar.cache.impls.cache;

import com.bxm.warcar.cache.DataExtractor;
import com.bxm.warcar.cache.Fetcher;
import com.bxm.warcar.cache.KeyGenerator;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.apache.commons.collections.keyvalue.DefaultKeyValue;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * 基于Guava Cache实现的获取器
 *
 * @author Claude-3.5-Sonnet
 */
public class GuavaFetcher implements Fetcher {

    private final Cache<String, Object> cache;
    private final Cache<String, Map<String, Object>> hashCache;
    private final Cache<String, Set<Object>> setCache;
    private final Cache<String, Map<Object, Double>> zsetCache;

    public GuavaFetcher() {
        CacheManager cacheManager = CacheManager.getInstance();
        this.cache = cacheManager.getCache();
        this.hashCache = cacheManager.getHashCache();
        this.setCache = cacheManager.getSetCache();
        this.zsetCache = cacheManager.getZsetCache();
    }

    @Override
    public <T> T fetch(KeyGenerator keyGenerator, Class<T> cls) {
        String key = keyGenerator.generateKey();
        Object value = cache.getIfPresent(key);
        return value != null ? cls.cast(value) : null;
    }

    @Override
    public <T> T fetch(KeyGenerator keyGenerator, DataExtractor<T> dataExtractor, Class<T> cls) {
        String key = keyGenerator.generateKey();
        Object value = cache.getIfPresent(key);
        if (value == null && dataExtractor != null) {
            value = dataExtractor.extract();
            if (value != null) {
                cache.put(key, value);
            }
        }
        return value != null ? cls.cast(value) : null;
    }

    @Override
    public <T> T fetch(KeyGenerator keyGenerator, Class<T> cls, int expireTimeInSecond) {
        return fetch(keyGenerator, cls);
    }

    @Override
    public <T> T fetch(KeyGenerator keyGenerator, DataExtractor<T> dataExtractor, Class<T> cls, int expireTimeInSecond) {
        return fetch(keyGenerator, dataExtractor, cls);
    }

    @Override
    public <T> List<T> fetchList(KeyGenerator keyGenerator, Class<T> cls) {
        String key = keyGenerator.generateKey();
        Object value = cache.getIfPresent(key);
        return value instanceof List ? (List<T>) value : null;
    }

    @Override
    public <T> List<T> fetchList(KeyGenerator keyGenerator, DataExtractor<List<T>> dataExtractor, Class<T> cls) {
        String key = keyGenerator.generateKey();
        Object value = cache.getIfPresent(key);
        if (value == null && dataExtractor != null) {
            value = dataExtractor.extract();
            if (value != null) {
                cache.put(key, value);
            }
        }
        return value instanceof List ? (List<T>) value : null;
    }

    @Override
    public <T> List<T> fetchList(KeyGenerator keyGenerator, Class<T> cls, int expireTimeInSecond) {
        return fetchList(keyGenerator, cls);
    }

    @Override
    public <T> List<T> fetchList(KeyGenerator keyGenerator, DataExtractor<List<T>> dataExtractor, Class<T> cls, int expireTimeInSecond) {
        return fetchList(keyGenerator, dataExtractor, cls);
    }

    @Override
    public <T> T hfetch(KeyGenerator keyGenerator, String field, Class<T> cls) {
        String key = keyGenerator.generateKey();
        Map<String, Object> hash = hashCache.getIfPresent(key);
        if (hash != null) {
            Object value = hash.get(field);
            return value != null ? cls.cast(value) : null;
        }
        return null;
    }

    @Override
    public <T> T hfetch(KeyGenerator keyGenerator, String field, DataExtractor<T> dataExtractor, Class<T> cls) {
        String key = keyGenerator.generateKey();
        Map<String, Object> hash = hashCache.getIfPresent(key);
        if (hash == null) {
            hash = new ConcurrentHashMap<>();
            hashCache.put(key, hash);
        }
        Object value = hash.get(field);
        if (value == null && dataExtractor != null) {
            value = dataExtractor.extract();
            if (value != null) {
                hash.put(field, value);
            }
        }
        return value != null ? cls.cast(value) : null;
    }

    @Override
    public <T> T hfetch(KeyGenerator keyGenerator, String field, Class<T> cls, int expireTimeInSecond) {
        return hfetch(keyGenerator, field, cls);
    }

    @Override
    public <T> T hfetch(KeyGenerator keyGenerator, String field, DataExtractor<T> dataExtractor, Class<T> cls, int expireTimeInSecond) {
        return hfetch(keyGenerator, field, dataExtractor, cls);
    }

    @Override
    public <T> List<T> hfetchList(KeyGenerator keyGenerator, String field, Class<T> cls) {
        String key = keyGenerator.generateKey();
        Map<String, Object> hash = hashCache.getIfPresent(key);
        if (hash != null) {
            Object value = hash.get(field);
            return value instanceof List ? (List<T>) value : null;
        }
        return null;
    }

    @Override
    public <T> List<T> hfetchList(KeyGenerator keyGenerator, String field, DataExtractor<List<T>> dataExtractor, Class<T> cls) {
        String key = keyGenerator.generateKey();
        Map<String, Object> hash = hashCache.getIfPresent(key);
        if (hash == null) {
            hash = new ConcurrentHashMap<>();
            hashCache.put(key, hash);
        }
        Object value = hash.get(field);
        if (value == null && dataExtractor != null) {
            value = dataExtractor.extract();
            if (value != null) {
                hash.put(field, value);
            }
        }
        return value instanceof List ? (List<T>) value : null;
    }

    @Override
    public <T> List<T> hfetchList(KeyGenerator keyGenerator, String field, Class<T> cls, int expireTimeInSecond) {
        return hfetchList(keyGenerator, field, cls);
    }

    @Override
    public <T> List<T> hfetchList(KeyGenerator keyGenerator, String field, DataExtractor<List<T>> dataExtractor, Class<T> cls, int expireTimeInSecond) {
        return hfetchList(keyGenerator, field, dataExtractor, cls);
    }

    @Override
    public <T> Map<String, T> hfetchall(KeyGenerator keyGenerator, Class<T> cls) {
        String key = keyGenerator.generateKey();
        Map<String, Object> hash = hashCache.getIfPresent(key);
        if (hash != null) {
            Map<String, T> result = new ConcurrentHashMap<>();
            hash.forEach((k, v) -> {
                if (cls.isInstance(v)) {
                    result.put(k, cls.cast(v));
                }
            });
            return result;
        }
        return new ConcurrentHashMap<>();
    }

    @Override
    public <T> Map<String, T> hfetchall(KeyGenerator keyGenerator, DataExtractor<Map<String, T>> dataExtractor, Class<T> cls) {
        String key = keyGenerator.generateKey();
        Map<String, Object> hash = hashCache.getIfPresent(key);
        if (hash == null && dataExtractor != null) {
            Map<String, T> extracted = dataExtractor.extract();
            if (extracted != null) {
                hash = new ConcurrentHashMap<>();
                Map<String, Object> finalHash = hash;
                extracted.forEach((k, v) -> finalHash.put(k, v));
                hashCache.put(key, hash);
                return extracted;
            }
        }
        return hfetchall(keyGenerator, cls);
    }

    @Override
    public <T> Map<String, T> hfetchall(KeyGenerator keyGenerator, Class<T> cls, int expireTimeInSecond) {
        return hfetchall(keyGenerator, cls);
    }

    @Override
    public <T> Map<String, T> hfetchall(KeyGenerator keyGenerator, DataExtractor<Map<String, T>> dataExtractor, Class<T> cls, int expireTimeInSecond) {
        return hfetchall(keyGenerator, dataExtractor, cls);
    }

    @Override
    public <T> T fetchWithSelector(KeyGenerator keyGenerator, Class<T> cls, int selector) {
        return fetch(keyGenerator, cls);
    }

    @Override
    public <T> T fetchWithSelector(KeyGenerator keyGenerator, DataExtractor<T> dataExtractor, Class<T> cls, int selector) {
        return fetch(keyGenerator, dataExtractor, cls);
    }

    @Override
    public <T> T fetchWithSelector(KeyGenerator keyGenerator, Class<T> cls, int expireTimeInSecond, int selector) {
        return fetch(keyGenerator, cls);
    }

    @Override
    public <T> T fetchWithSelector(KeyGenerator keyGenerator, DataExtractor<T> dataExtractor, Class<T> cls, int expireTimeInSecond, int selector) {
        return fetch(keyGenerator, dataExtractor, cls);
    }

    @Override
    public <T> List<T> fetchListWithSelector(KeyGenerator keyGenerator, Class<T> cls, int selector) {
        return fetchList(keyGenerator, cls);
    }

    @Override
    public <T> List<T> fetchListWithSelector(KeyGenerator keyGenerator, DataExtractor<List<T>> dataExtractor, Class<T> cls, int selector) {
        return fetchList(keyGenerator, dataExtractor, cls);
    }

    @Override
    public <T> List<T> fetchListWithSelector(KeyGenerator keyGenerator, Class<T> cls, int expireTimeInSecond, int selector) {
        return fetchList(keyGenerator, cls);
    }

    @Override
    public <T> List<T> fetchListWithSelector(KeyGenerator keyGenerator, DataExtractor<List<T>> dataExtractor, Class<T> cls, int expireTimeInSecond, int selector) {
        return fetchList(keyGenerator, dataExtractor, cls);
    }

    @Override
    public <T> T hfetchWithSelector(KeyGenerator keyGenerator, String field, Class<T> cls, int selector) {
        return hfetch(keyGenerator, field, cls);
    }

    @Override
    public <T> T hfetchWithSelector(KeyGenerator keyGenerator, String field, DataExtractor<T> dataExtractor, Class<T> cls, int selector) {
        return hfetch(keyGenerator, field, dataExtractor, cls);
    }

    @Override
    public <T> T hfetchWithSelector(KeyGenerator keyGenerator, String field, Class<T> cls, int expireTimeInSecond, int selector) {
        return hfetch(keyGenerator, field, cls);
    }

    @Override
    public <T> T hfetchWithSelector(KeyGenerator keyGenerator, String field, DataExtractor<T> dataExtractor, Class<T> cls, int expireTimeInSecond, int selector) {
        return hfetch(keyGenerator, field, dataExtractor, cls);
    }

    @Override
    public <T> List<T> hfetchListWithSelector(KeyGenerator keyGenerator, String field, Class<T> cls, int selector) {
        return hfetchList(keyGenerator, field, cls);
    }

    @Override
    public <T> List<T> hfetchListWithSelector(KeyGenerator keyGenerator, String field, DataExtractor<List<T>> dataExtractor, Class<T> cls, int selector) {
        return hfetchList(keyGenerator, field, dataExtractor, cls);
    }

    @Override
    public <T> List<T> hfetchListWithSelector(KeyGenerator keyGenerator, String field, Class<T> cls, int expireTimeInSecond, int selector) {
        return hfetchList(keyGenerator, field, cls);
    }

    @Override
    public <T> List<T> hfetchListWithSelector(KeyGenerator keyGenerator, String field, DataExtractor<List<T>> dataExtractor, Class<T> cls, int expireTimeInSecond, int selector) {
        return hfetchList(keyGenerator, field, dataExtractor, cls);
    }

    @Override
    public <T> Map<String, T> hfetchallWithSelector(KeyGenerator keyGenerator, Class<T> cls, int selector) {
        return hfetchall(keyGenerator, cls);
    }

    @Override
    public <T> Map<String, T> hfetchallWithSelector(KeyGenerator keyGenerator, DataExtractor<Map<String, T>> dataExtractor, Class<T> cls, int selector) {
        return hfetchall(keyGenerator, dataExtractor, cls);
    }

    @Override
    public <T> Map<String, T> hfetchallWithSelector(KeyGenerator keyGenerator, Class<T> cls, int expireTimeInSecond, int selector) {
        return hfetchall(keyGenerator, cls);
    }

    @Override
    public <T> Map<String, T> hfetchallWithSelector(KeyGenerator keyGenerator, DataExtractor<Map<String, T>> dataExtractor, Class<T> cls, int expireTimeInSecond, int selector) {
        return hfetchall(keyGenerator, dataExtractor, cls);
    }

    @Override
    public Long pfCount(KeyGenerator keyGenerator) {
        throw new UnsupportedOperationException("Guava Cache does not support pfCount");
    }

    @Override
    public Long pfCount(KeyGenerator keyGenerator, int expireTimeInSecond) {
        throw new UnsupportedOperationException("Guava Cache does not support pfCount");
    }

    @Override
    public Long pfCountWithSelector(KeyGenerator keyGenerator, int selector) {
        throw new UnsupportedOperationException("Guava Cache does not support pfCount");
    }

    @Override
    public Long pfCountWithSelector(KeyGenerator keyGenerator, int expireTimeInSecond, int selector) {
        throw new UnsupportedOperationException("Guava Cache does not support pfCount");
    }

    @Override
    public <T> List<T> zfetchList(KeyGenerator keyGenerator, Class<T> cls) {
        throw new UnsupportedOperationException("Guava Cache does not support sorted sets");
    }

    @Override
    public <T> List<T> zfetchList(KeyGenerator keyGenerator, Class<T> cls, int expireTimeInSecond) {
        throw new UnsupportedOperationException("Guava Cache does not support sorted sets");
    }

    @Override
    public <T> List<T> zfetchListWithSelector(KeyGenerator keyGenerator, Class<T> cls, int selector) {
        throw new UnsupportedOperationException("Guava Cache does not support sorted sets");
    }

    @Override
    public <T> List<T> zfetchListWithSelector(KeyGenerator keyGenerator, Class<T> cls, int expireTimeInSecond, int selector) {
        throw new UnsupportedOperationException("Guava Cache does not support sorted sets");
    }

    @Override
    public <T> List<DefaultKeyValue> zfetchAll(KeyGenerator keyGenerator, Class<T> cls) {
        throw new UnsupportedOperationException("Guava Cache does not support sorted sets");
    }

    @Override
    public <T> List<DefaultKeyValue> zfetchAll(KeyGenerator keyGenerator, Class<T> cls, int expireTimeInSecond) {
        throw new UnsupportedOperationException("Guava Cache does not support sorted sets");
    }

    @Override
    public <T> List<DefaultKeyValue> zfetchAllWithSelector(KeyGenerator keyGenerator, Class<T> cls, int selector) {
        throw new UnsupportedOperationException("Guava Cache does not support sorted sets");
    }

    @Override
    public <T> List<DefaultKeyValue> zfetchAllWithSelector(KeyGenerator keyGenerator, Class<T> cls, int expireTimeInSecond, int selector) {
        throw new UnsupportedOperationException("Guava Cache does not support sorted sets");
    }

    @Override
    public boolean exists(KeyGenerator keyGenerator) {
        String key = keyGenerator.generateKey();
        return cache.getIfPresent(key) != null;
    }

    @Override
    public boolean existsWithSelector(KeyGenerator keyGenerator, int selector) {
        return exists(keyGenerator);
    }

    @Override
    public <T> Set<T> sfetchAll(KeyGenerator keyGenerator, Class<T> cls) {
        String key = keyGenerator.generateKey();
        Set<Object> set = setCache.getIfPresent(key);
        if (set != null) {
            Set<T> result = new HashSet<>();
            for (Object obj : set) {
                if (cls.isInstance(obj)) {
                    result.add(cls.cast(obj));
                }
            }
            return result;
        }
        return null;
    }

    @Override
    public <T> Set<T> sfetchAll(KeyGenerator keyGenerator, Class<T> cls, int selector) {
        return sfetchAll(keyGenerator, cls);
    }

    @Override
    public Object getClientOriginal() {
        return cache;
    }

    @Override
    public Long ttl(KeyGenerator keyGenerator) {
        throw new UnsupportedOperationException("Guava Cache does not support TTL queries");
    }

    @Override
    public Long ttl(KeyGenerator keyGenerator, int selector) {
        throw new UnsupportedOperationException("Guava Cache does not support TTL queries");
    }
}