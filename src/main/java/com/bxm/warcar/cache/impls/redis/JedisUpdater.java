package com.bxm.warcar.cache.impls.redis;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.bxm.warcar.cache.KeyGenerator;
import com.bxm.warcar.cache.Updater;
import com.bxm.warcar.cache.serialization.JSONSerialization;
import com.google.common.base.Preconditions;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * @author allen
 * @date 2017-12-04
 */
public class JedisUpdater implements Updater {

    private final JedisPool jedisPool;
    private final JSONSerialization serialization;

    public JedisUpdater(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
        this.serialization = new JSONSerialization();
    }

    @Override
    public Object getClientOriginal() {
        return jedisPool;
    }

    @Override
    public void expire(KeyGenerator keyGenerator, int seconds) {
        Preconditions.checkNotNull(keyGenerator);
        String key = keyGenerator.generateKey();
        if (StringUtils.isBlank(key)) {
            throw new NullPointerException("key");
        }

        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.expire(key, seconds);
        }
        finally {
            if (null != jedis) {
                jedis.close();
            }
        }
    }

    @Override
    public void update(KeyGenerator keyGenerator, Object value, int expireTimeInSecond) {
        this.updateWithSelector(keyGenerator, value, expireTimeInSecond, -1);
    }

    @Override
    public void update(KeyGenerator keyGenerator, Object value) {
        this.update(keyGenerator, value, 0);
    }

    @Override
    public void updateWithSelector(KeyGenerator keyGenerator, Object value, int selector) {
        this.updateWithSelector(keyGenerator, value, 0, selector);
    }

    @Override
    public void updateWithSelector(KeyGenerator keyGenerator, Object value, int expireTimeInSecond, int selector) {
        Preconditions.checkNotNull(keyGenerator);
        String key = keyGenerator.generateKey();
        if (StringUtils.isBlank(key)) {
            throw new NullPointerException("key");
        }
        if (null == value) {
            throw new NullPointerException("value");
        }

        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            if (selector >= 0) {
                jedis.select(selector);
            }
            jedis.set(key, this.serialization.serialize(value));
            if (expireTimeInSecond > 0) {
                jedis.expire(key, expireTimeInSecond);
            }
        }
        finally {
            if (null != jedis) {
                jedis.close();
            }
        }
    }

    @Override
    public void remove(KeyGenerator keyGenerator) {
        this.removeWithSelector(keyGenerator, -1);
    }

    @Override
    public void removeWithSelector(KeyGenerator keyGenerator, int selector) {
        Preconditions.checkNotNull(keyGenerator);
        String key = keyGenerator.generateKey();
        if (StringUtils.isBlank(key)) {
            throw new NullPointerException("key");
        }

        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            if (selector >= 0) {
                jedis.select(selector);
            }
            jedis.del(key);
        }
        finally {
            if (null != jedis) {
                jedis.close();
            }
        }
    }

    @Override
    public void hupdate(KeyGenerator keyGenerator, String field, Object value) {
        this.hupdate(keyGenerator, field, value, 0);
    }

    @Override
    public void hupdate(KeyGenerator keyGenerator, String field, Object value, int expireTimeInSecond) {
        this.hupdateWithSelector(keyGenerator, field, value, expireTimeInSecond, -1);
    }

    @Override
    public void hupdateWithSelector(KeyGenerator keyGenerator, String field, Object value, int selector) {
        this.hupdateWithSelector(keyGenerator, field, value, 0, selector);
    }

    @Override
    public void hupdateWithSelector(KeyGenerator keyGenerator, String field, Object value, int expireTimeInSecond, int selector) {
        Preconditions.checkNotNull(keyGenerator);
        String key = keyGenerator.generateKey();
        if (StringUtils.isBlank(key)) {
            throw new NullPointerException("key");
        }
        if (null == value) {
            throw new NullPointerException("value");
        }
        if (StringUtils.isBlank(field)) {
            throw new NullPointerException("field");
        }

        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            if (selector >= 0) {
                jedis.select(selector);
            }
            jedis.hset(key, field, this.serialization.serialize(value));
            if (expireTimeInSecond > 0) {
                jedis.expire(key, expireTimeInSecond);
            }
        }
        finally {
            if (null != jedis) {
                jedis.close();
            }
        }
    }

    @Override
    public void hremove(KeyGenerator keyGenerator, String...field) {
        this.hremoveWithSelector(keyGenerator, -1, field);
    }

    @Override
    public void hremoveWithSelector(KeyGenerator keyGenerator, int selector, String... field) {
        Preconditions.checkNotNull(keyGenerator);
        String key = keyGenerator.generateKey();
        if (StringUtils.isBlank(key)) {
            throw new NullPointerException("key");
        }

        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            if (selector >= 0) {
                jedis.select(selector);
            }
            jedis.hdel(key, field);
        }
        finally {
            if (null != jedis) {
                jedis.close();
            }
        }
    }

    @Override
    public void hmupdate(KeyGenerator keyGenerator, Map<String, String> hash) {
        this.hmupdate(keyGenerator, hash, 0);
    }

    @Override
    public void hmupdate(KeyGenerator keyGenerator, Map<String, String> hash, int expireTimeInSecond) {
        this.hmupdateWithSelector(keyGenerator, hash, expireTimeInSecond, -1);
    }

    @Override
    public void hmupdateWithSelector(KeyGenerator keyGenerator, Map<String, String> hash, int selector) {
        this.hmupdateWithSelector(keyGenerator, hash, 0, selector);
    }

    @Override
    public void hmupdateWithSelector(KeyGenerator keyGenerator, Map<String, String> hash, int expireTimeInSecond, int selector) {
        Preconditions.checkNotNull(keyGenerator);
        String key = keyGenerator.generateKey();
        if (StringUtils.isBlank(key)) {
            throw new NullPointerException("key");
        }
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            if (selector >= 0) {
                jedis.select(selector);
            }
            jedis.hmset(key, hash);
            if (expireTimeInSecond > 0) {
                jedis.expire(key, expireTimeInSecond);
            }
        }
        finally {
            if (null != jedis) {
                jedis.close();
            }
        }
    }

    @Override
    public void zupdate(KeyGenerator keyGenerator, Double score, Object member) {
        zupdate(keyGenerator,score,member,-1);
    }

    @Override
    public void zupdate(KeyGenerator keyGenerator, Double score, Object member, int expireTimeInSecond) {
        zupdateWithSelector(keyGenerator,score,member,expireTimeInSecond,-1);
    }

    @Override
    public void zupdateWithSelector(KeyGenerator keyGenerator, Double score, Object member, int selector) {
        zupdateWithSelector(keyGenerator,score,member,-1,selector);
    }

    @Override
    public void zupdateWithSelector(KeyGenerator keyGenerator, Double score, Object member, int expireTimeInSecond, int selector) {
        Preconditions.checkNotNull(keyGenerator);
        String key = keyGenerator.generateKey();
        if (StringUtils.isBlank(key)) {
            throw new NullPointerException("key");
        }
        if (null == score) {
            throw new NullPointerException("score");
        }
        if (null == member) {
            throw new NullPointerException("member");
        }

        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            if (selector >= 0) {
                jedis.select(selector);
            }
            jedis.zadd(key,score,this.serialization.serialize(member));
            if (expireTimeInSecond > 0) {
                jedis.expire(key, expireTimeInSecond);
            }
        }
        finally {
            if (null != jedis) {
                jedis.close();
            }
        }
    }

    @Override
    public void zmupdate(KeyGenerator keyGenerator, Map<String, Double> hash) {
        zmupdate(keyGenerator,hash,-1);
    }

    @Override
    public void zmupdate(KeyGenerator keyGenerator, Map<String, Double> hash, int expireTimeInSecond) {
        zmupdateWithSelector(keyGenerator,hash,expireTimeInSecond,-1);
    }

    @Override
    public void zmupdateWithSelector(KeyGenerator keyGenerator, Map<String, Double> hash, int selector) {
        zmupdateWithSelector(keyGenerator,hash,-1,selector);
    }

    @Override
    public void zmupdateWithSelector(KeyGenerator keyGenerator, Map<String, Double> hash, int expireTimeInSecond, int selector) {
        Preconditions.checkNotNull(keyGenerator);
        String key = keyGenerator.generateKey();
        if (StringUtils.isBlank(key)) {
            throw new NullPointerException("key");
        }
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            if (selector >= 0) {
                jedis.select(selector);
            }
            jedis.zadd(key,hash);
            if (expireTimeInSecond > 0) {
                jedis.expire(key, expireTimeInSecond);
            }
        }
        finally {
            if (null != jedis) {
                jedis.close();
            }
        }
    }

    @Override
    public void zremove(KeyGenerator keyGenerator, String... field) {
        zremoveWithSelector(keyGenerator,-1,field);
    }

    @Override
    public void zremoveWithSelector(KeyGenerator keyGenerator, int selector, String... field) {
        Preconditions.checkNotNull(keyGenerator);
        String key = keyGenerator.generateKey();
        if (StringUtils.isBlank(key)) {
            throw new NullPointerException("key");
        }

        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            if (selector >= 0) {
                jedis.select(selector);
            }
            jedis.zrem(key,field);
        }
        finally {
            if (null != jedis) {
                jedis.close();
            }
        }
    }

    @Override
    public void supdate(KeyGenerator keyGenerator, String... member) {
        supdate(keyGenerator,-1,member);
    }

    @Override
    public void supdate(KeyGenerator keyGenerator, int expireTimeInSecond, String... member) {
        supdateWithSelector(keyGenerator,expireTimeInSecond,-1,member);
    }

    @Override
    public void supdateWithSelector(KeyGenerator keyGenerator, int selector, String... member) {
        supdateWithSelector(keyGenerator,-1,selector,member);
    }

    @Override
    public void supdateWithSelector(KeyGenerator keyGenerator, int expireTimeInSecond, int selector, String... member) {
        Preconditions.checkNotNull(keyGenerator);
        String key = keyGenerator.generateKey();
        if (StringUtils.isBlank(key)) {
            throw new NullPointerException("key");
        }
        if (null == member) {
            throw new NullPointerException("member");
        }

        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            if (selector >= 0) {
                jedis.select(selector);
            }
            jedis.sadd(key,member);
            if (expireTimeInSecond > 0) {
                jedis.expire(key, expireTimeInSecond);
            }
        }
        finally {
            if (null != jedis) {
                jedis.close();
            }
        }
    }

    @Override
    public void srem(KeyGenerator keyGenerator, String... member) {
        sremWithSelector(keyGenerator,-1,member);
    }

    @Override
    public void sremWithSelector(KeyGenerator keyGenerator, int selector, String... member) {
        Preconditions.checkNotNull(keyGenerator);
        String key = keyGenerator.generateKey();
        if (StringUtils.isBlank(key)) {
            throw new NullPointerException("key");
        }

        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            if (selector >= 0) {
                jedis.select(selector);
            }
            jedis.srem(key,member);
        }
        finally {
            if (null != jedis) {
                jedis.close();
            }
        }
    }
}
