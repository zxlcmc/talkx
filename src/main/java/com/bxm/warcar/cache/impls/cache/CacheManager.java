package com.bxm.warcar.cache.impls.cache;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 缓存管理器，统一管理Guava Cache实例
 *
 * @author Claude-3.5-Sonnet
 */
public class CacheManager {
    private static final CacheManager INSTANCE = new CacheManager();

    // 基础缓存
    private final Cache<String, Object> cache;
    // Hash类型缓存
    private final Cache<String, Map<String, Object>> hashCache;
    // Set类型缓存
    private final Cache<String, Set<Object>> setCache;
    // ZSet类型缓存
    private final Cache<String, Map<Object, Double>> zsetCache;

    private CacheManager() {
        this.cache = CacheBuilder.newBuilder().build();
        this.hashCache = CacheBuilder.newBuilder().build();
        this.setCache = CacheBuilder.newBuilder().build();
        this.zsetCache = CacheBuilder.newBuilder().build();
    }

    public static CacheManager getInstance() {
        return INSTANCE;
    }

    public Cache<String, Object> getCache() {
        return cache;
    }

    public Cache<String, Map<String, Object>> getHashCache() {
        return hashCache;
    }

    public Cache<String, Set<Object>> getSetCache() {
        return setCache;
    }

    public Cache<String, Map<Object, Double>> getZsetCache() {
        return zsetCache;
    }
}