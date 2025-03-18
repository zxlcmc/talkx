package com.bxm.warcar.cache.impls.cache;

import com.bxm.warcar.cache.KeyGenerator;
import com.bxm.warcar.cache.Updater;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * 基于Guava Cache实现的更新器
 *
 * @author Claude-3.5-Sonnet
 */
public class GuavaUpdater implements Updater {

    private final Cache<String, Object> cache;
    private final Cache<String, Map<String, Object>> hashCache;
    private final Cache<String, Map<Object, Double>> zsetCache;
    private final Cache<String, Set<String>> setCache;

    public GuavaUpdater() {
        CacheManager cacheManager = CacheManager.getInstance();
        this.cache = cacheManager.getCache();
        this.hashCache = cacheManager.getHashCache();
        this.zsetCache = cacheManager.getZsetCache();
        this.setCache = (Cache) cacheManager.getSetCache();
    }

    @Override
    public void expire(KeyGenerator keyGenerator, int seconds) {
        // Guava Cache不支持动态设置过期时间，这里不做处理
    }

    @Override
    public void update(KeyGenerator keyGenerator, Object value) {
        String key = keyGenerator.generateKey();
        cache.put(key, value);
    }

    @Override
    public void update(KeyGenerator keyGenerator, Object value, int expireTimeInSecond) {
        update(keyGenerator, value);
    }

    @Override
    public void updateWithSelector(KeyGenerator keyGenerator, Object value, int selector) {
        update(keyGenerator, value);
    }

    @Override
    public void updateWithSelector(KeyGenerator keyGenerator, Object value, int expireTimeInSecond, int selector) {
        update(keyGenerator, value);
    }

    @Override
    public void remove(KeyGenerator keyGenerator) {
        String key = keyGenerator.generateKey();
        cache.invalidate(key);
    }

    @Override
    public void removeWithSelector(KeyGenerator keyGenerator, int selector) {
        remove(keyGenerator);
    }

    @Override
    public void hupdate(KeyGenerator keyGenerator, String field, Object value) {
        String key = keyGenerator.generateKey();
        Map<String, Object> hash = hashCache.getIfPresent(key);
        if (hash == null) {
            hash = new ConcurrentHashMap<>();
            hashCache.put(key, hash);
        }
        hash.put(field, value);
    }

    @Override
    public void hupdate(KeyGenerator keyGenerator, String field, Object value, int expireTimeInSecond) {
        hupdate(keyGenerator, field, value);
    }

    @Override
    public void hupdateWithSelector(KeyGenerator keyGenerator, String field, Object value, int selector) {
        hupdate(keyGenerator, field, value);
    }

    @Override
    public void hupdateWithSelector(KeyGenerator keyGenerator, String field, Object value, int expireTimeInSecond, int selector) {
        hupdate(keyGenerator, field, value);
    }

    @Override
    public void hremove(KeyGenerator keyGenerator, String... field) {
        String key = keyGenerator.generateKey();
        Map<String, Object> hash = hashCache.getIfPresent(key);
        if (hash != null && field != null) {
            for (String f : field) {
                hash.remove(f);
            }
        }
    }

    @Override
    public void hremoveWithSelector(KeyGenerator keyGenerator, int selector, String... field) {
        hremove(keyGenerator, field);
    }

    @Override
    public void hmupdate(KeyGenerator keyGenerator, Map<String, String> hash) {
        String key = keyGenerator.generateKey();
        Map<String, Object> existingHash = hashCache.getIfPresent(key);
        if (existingHash == null) {
            existingHash = new ConcurrentHashMap<>();
            hashCache.put(key, existingHash);
        }
        existingHash.putAll(hash);
    }

    @Override
    public void hmupdateWithSelector(KeyGenerator keyGenerator, Map<String, String> hash, int selector) {
        hmupdate(keyGenerator, hash);
    }

    @Override
    public void hmupdate(KeyGenerator keyGenerator, Map<String, String> hash, int expireTimeInSecond) {
        hmupdate(keyGenerator, hash);
    }

    @Override
    public void hmupdateWithSelector(KeyGenerator keyGenerator, Map<String, String> hash, int expireTimeInSecond, int selector) {
        hmupdate(keyGenerator, hash);
    }

    @Override
    public void zupdate(KeyGenerator keyGenerator, Double score, Object member) {
        String key = keyGenerator.generateKey();
        Map<Object, Double> zset = zsetCache.getIfPresent(key);
        if (zset == null) {
            zset = new ConcurrentHashMap<>();
            zsetCache.put(key, zset);
        }
        zset.put(member, score);
    }

    @Override
    public void zupdate(KeyGenerator keyGenerator, Double score, Object member, int expireTimeInSecond) {
        zupdate(keyGenerator, score, member);
    }

    @Override
    public void zupdateWithSelector(KeyGenerator keyGenerator, Double score, Object member, int selector) {
        zupdate(keyGenerator, score, member);
    }

    @Override
    public void zupdateWithSelector(KeyGenerator keyGenerator, Double score, Object member, int expireTimeInSecond, int selector) {
        zupdate(keyGenerator, score, member);
    }

    @Override
    public void zmupdate(KeyGenerator keyGenerator, Map<String, Double> hash) {
        String key = keyGenerator.generateKey();
        Map<Object, Double> zset = zsetCache.getIfPresent(key);
        if (zset == null) {
            zset = new ConcurrentHashMap<>();
            zsetCache.put(key, zset);
        }
        hash.forEach(zset::put);
    }

    @Override
    public void zmupdate(KeyGenerator keyGenerator, Map<String, Double> hash, int expireTimeInSecond) {
        zmupdate(keyGenerator, hash);
    }

    @Override
    public void zmupdateWithSelector(KeyGenerator keyGenerator, Map<String, Double> hash, int selector) {
        zmupdate(keyGenerator, hash);
    }

    @Override
    public void zmupdateWithSelector(KeyGenerator keyGenerator, Map<String, Double> hash, int expireTimeInSecond, int selector) {
        zmupdate(keyGenerator, hash);
    }

    @Override
    public void zremove(KeyGenerator keyGenerator, String... field) {
        String key = keyGenerator.generateKey();
        Map<Object, Double> zset = zsetCache.getIfPresent(key);
        if (zset != null && field != null) {
            for (String f : field) {
                zset.remove(f);
            }
        }
    }

    @Override
    public void zremoveWithSelector(KeyGenerator keyGenerator, int selector, String... field) {
        zremove(keyGenerator, field);
    }

    @Override
    public void supdate(KeyGenerator keyGenerator, String... member) {
        String key = keyGenerator.generateKey();
        Set<String> set = setCache.getIfPresent(key);
        if (set == null) {
            set = ConcurrentHashMap.newKeySet();
            setCache.put(key, set);
        }
        if (member != null) {
            Collections.addAll(set, member);
        }
    }

    @Override
    public void supdate(KeyGenerator keyGenerator, int expireTimeInSecond, String... member) {
        supdate(keyGenerator, member);
    }

    @Override
    public void supdateWithSelector(KeyGenerator keyGenerator, int selector, String... member) {
        supdate(keyGenerator, member);
    }

    @Override
    public void supdateWithSelector(KeyGenerator keyGenerator, int expireTimeInSecond, int selector, String... member) {
        supdate(keyGenerator, member);
    }

    @Override
    public void srem(KeyGenerator keyGenerator, String... member) {
        String key = keyGenerator.generateKey();
        Set<String> set = setCache.getIfPresent(key);
        if (set != null && member != null) {
            for (String m : member) {
                set.remove(m);
            }
        }
    }

    @Override
    public void sremWithSelector(KeyGenerator keyGenerator, int selector, String... member) {
        srem(keyGenerator, member);
    }

    @Override
    public Object getClientOriginal() {
        return this;
    }
}