package com.bxm.warcar.cache.impls.cache;

import com.bxm.warcar.cache.Counter;
import com.bxm.warcar.cache.KeyGenerator;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 基于Guava Cache实现的计数器
 *
 * @author Claude-3.5-Sonnet
 */
public class GuavaCounter implements Counter {

    private final Cache<String, AtomicLong> counterCache;
    private final Cache<String, AtomicReference<Double>> floatCounterCache;
    private final Cache<String, Map<String, AtomicLong>> hashCounterCache;
    private final Cache<String, Map<String, AtomicReference<Double>>> hashFloatCounterCache;

    public GuavaCounter() {
        CacheManager cacheManager = CacheManager.getInstance();
        this.counterCache = (Cache) cacheManager.getCache();
        this.floatCounterCache = (Cache) cacheManager.getCache();
        this.hashCounterCache = (Cache) cacheManager.getHashCache();
        this.hashFloatCounterCache = (Cache) cacheManager.getHashCache();
    }

    @Override
    public Long incrementAndGet(KeyGenerator keyGenerator) {
        return incrementByAndGet(keyGenerator, 1L);
    }

    @Override
    public Long incrementAndGet(KeyGenerator keyGenerator, int expireTimeInSecond) {
        return incrementByAndGet(keyGenerator, 1L, expireTimeInSecond);
    }

    @Override
    public Long incrementByAndGet(KeyGenerator keyGenerator, long inc) {
        String key = keyGenerator.generateKey();
        AtomicLong counter = counterCache.getIfPresent(key);
        if (counter == null) {
            counter = new AtomicLong(0);
            counterCache.put(key, counter);
        }
        return counter.addAndGet(inc);
    }

    @Override
    public Long incrementByAndGet(KeyGenerator keyGenerator, long inc, int expireTimeInSecond) {
        // Guava Cache已经通过CacheBuilder设置了过期时间，这里直接调用无过期时间的方法
        return incrementByAndGet(keyGenerator, inc);
    }

    @Override
    public Double incrFloatByAndGet(KeyGenerator keyGenerator, double inc) {
        String key = keyGenerator.generateKey();
        AtomicReference<Double> counter = floatCounterCache.getIfPresent(key);
        if (counter == null) {
            counter = new AtomicReference<>(0.0);
            floatCounterCache.put(key, counter);
        }
        while (true) {
            Double current = counter.get();
            Double next = current + inc;
            if (counter.compareAndSet(current, next)) {
                return next;
            }
        }
    }

    @Override
    public Double incrFloatByAndGet(KeyGenerator keyGenerator, double inc, int expireTimeInSecond) {
        return incrFloatByAndGet(keyGenerator, inc);
    }

    @Override
    public Long decrementAndGet(KeyGenerator keyGenerator) {
        return decrementByAndGet(keyGenerator, 1L);
    }

    @Override
    public Long decrementAndGet(KeyGenerator keyGenerator, int expireTimeInSecond) {
        return decrementByAndGet(keyGenerator, 1L, expireTimeInSecond);
    }

    @Override
    public Long decrementByAndGet(KeyGenerator keyGenerator, long dec) {
        return incrementByAndGet(keyGenerator, -dec);
    }

    @Override
    public Long decrementByAndGet(KeyGenerator keyGenerator, long dec, int expireTimeInSecond) {
        return incrementByAndGet(keyGenerator, -dec, expireTimeInSecond);
    }

    @Override
    public void set(KeyGenerator keyGenerator, long value) {
        String key = keyGenerator.generateKey();
        counterCache.put(key, new AtomicLong(value));
    }

    @Override
    public void expire(KeyGenerator keyGenerator, int seconds) {
        // Guava Cache不支持动态设置过期时间，这里不做处理
    }

    @Override
    public Long get(KeyGenerator keyGenerator) {
        String key = keyGenerator.generateKey();
        AtomicLong counter = counterCache.getIfPresent(key);
        return counter != null ? counter.get() : null;
    }

    @Override
    public Long hincrementAndGet(KeyGenerator keyGenerator, String field) {
        return hincrementByAndGet(keyGenerator, field, 1L);
    }

    @Override
    public Long hincrementAndGet(KeyGenerator keyGenerator, String field, int expireTimeInSecond) {
        return hincrementByAndGet(keyGenerator, field, 1L, expireTimeInSecond);
    }

    @Override
    public Long hincrementByAndGet(KeyGenerator keyGenerator, String field, long inc) {
        String key = keyGenerator.generateKey();
        Map<String, AtomicLong> hashCounter = hashCounterCache.getIfPresent(key);
        if (hashCounter == null) {
            hashCounter = new ConcurrentHashMap<>();
            hashCounterCache.put(key, hashCounter);
        }
        AtomicLong counter = hashCounter.computeIfAbsent(field, k -> new AtomicLong(0));
        return counter.addAndGet(inc);
    }

    @Override
    public Long hincrementByAndGet(KeyGenerator keyGenerator, String field, long inc, int expireTimeInSecond) {
        return hincrementByAndGet(keyGenerator, field, inc);
    }

    @Override
    public Double hincrFloatByAndGet(KeyGenerator keyGenerator, String field, double inc) {
        String key = keyGenerator.generateKey();
        Map<String, AtomicReference<Double>> hashCounter = hashFloatCounterCache.getIfPresent(key);
        if (hashCounter == null) {
            hashCounter = new ConcurrentHashMap<>();
            hashFloatCounterCache.put(key, hashCounter);
        }
        AtomicReference<Double> counter = hashCounter.computeIfAbsent(field, k -> new AtomicReference<>(0.0));
        while (true) {
            Double current = counter.get();
            Double next = current + inc;
            if (counter.compareAndSet(current, next)) {
                return next;
            }
        }
    }

    @Override
    public Double hincrFloatByAndGet(KeyGenerator keyGenerator, String field, double inc, int expireTimeInSecond) {
        return hincrFloatByAndGet(keyGenerator, field, inc);
    }

    @Override
    public Long hget(KeyGenerator keyGenerator, String field) {
        String key = keyGenerator.generateKey();
        Map<String, AtomicLong> hashCounter = hashCounterCache.getIfPresent(key);
        if (hashCounter != null) {
            AtomicLong counter = hashCounter.get(field);
            return counter != null ? counter.get() : null;
        }
        return null;
    }

    @Override
    public Map<String, Long> hgetall(KeyGenerator keyGenerator) {
        String key = keyGenerator.generateKey();
        Map<String, AtomicLong> hashCounter = hashCounterCache.getIfPresent(key);
        if (hashCounter != null) {
            Map<String, Long> result = new ConcurrentHashMap<>();
            hashCounter.forEach((field, counter) -> result.put(field, counter.get()));
            return result;
        }
        return new ConcurrentHashMap<>();
    }

    @Override
    public Object getClientOriginal() {
        return this;
    }
}