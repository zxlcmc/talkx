/*
 * Copyright 2016 bianxianmao.com All right reserved. This software is the confidential and proprietary information of
 * textile.com ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with bianxianmao.com.
 */

package com.bxm.warcar.id.redis;

import com.bxm.warcar.id.IdGenerator;
import com.bxm.warcar.id.IdUtils;
import com.bxm.warcar.utils.NamedThreadFactory;
import com.google.common.base.Preconditions;
import org.apache.commons.lang3.StringUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Protocol;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 使用 Redis 64位（bit）有符号数字范围。从 0 开始到 9223372036854775807 <br>
 * 在并发每秒 10000 的情况下可以使用 29,653,330 年
 *
 * @author allen
 * @since 1.0.0
 */
public class RedisIdGenerator implements IdGenerator {

    private static final int DEFAULT_LEN = 32;
    private static final String DEFAULT_KEY = "com.bxm.warcar.id";

    /**
     * ID 长度
     */
    private int len;
    private final String key;
    private final String prefix;
    private final JedisPool jedisPool;

    public RedisIdGenerator(JedisPool jedisPool) {
        this(DEFAULT_KEY, null, jedisPool);
    }

    public RedisIdGenerator(String prefix, JedisPool jedisPool) {
        this(DEFAULT_LEN, DEFAULT_KEY, prefix, jedisPool);
    }

    public RedisIdGenerator(String key, String prefix, JedisPool jedisPool) {
        this(DEFAULT_LEN, key, prefix, jedisPool);
    }

    public RedisIdGenerator(int len, String key, String prefix, JedisPool jedisPool) {
        Preconditions.checkArgument(len > 20, "id length must be > 20");
        this.len = len;
        this.key = key;
        this.prefix = prefix;
        this.jedisPool = jedisPool;
    }

    @Override
    public String next() {
        Jedis jedis = jedisPool.getResource();
        try {
            Long value = jedis.incr(key);
            String mid = getDateTime();
            int midLen = StringUtils.length(mid) + StringUtils.length(prefix);
            int i = len - midLen;
            return StringUtils.defaultIfBlank(prefix, "") + mid
                    + StringUtils.leftPad(StringUtils.right(String.valueOf(value), i), i, '0');
        } finally {
            if (null != jedis) {
                jedis.close();
            }
        }
    }

    private String getDateTime() {
        return IdUtils.getDateTime();
    }

    public static void main(String[] args) {
        System.out.println(Long.MAX_VALUE);

        JedisPool jedisPool = new JedisPool(new JedisPoolConfig(), "47.97.189.222", 6379, Protocol.DEFAULT_TIMEOUT, "redis_pwd123");
        RedisIdGenerator generator = new RedisIdGenerator(32, "com.bxm.warcar.id.guide", "lti.", jedisPool);
//        long start = System.currentTimeMillis();
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(100, 100,
                0, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(), new NamedThreadFactory("pool"));
        for (int i = 0; i < 10000; i++) {
            threadPoolExecutor.execute(() -> {
                System.out.println(generator.next());
                try {
                    Thread.sleep(5000L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
//        System.out.println((System.currentTimeMillis() - start));
//
//        start = System.currentTimeMillis();
//        System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS")));
//        System.out.println((System.currentTimeMillis() - start));
//
//        start = System.currentTimeMillis();
//        Calendar calendar = Calendar.getInstance();
//        String mid = StringUtils.join(new Object[] {
//                calendar.get(Calendar.YEAR),
//                StringUtils.leftPad(String.valueOf(calendar.get(Calendar.MONTH) + 1), 2, '0'),
//                StringUtils.leftPad(String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)), 2, '0'),
//                calendar.get(Calendar.HOUR_OF_DAY),
//                calendar.get(Calendar.MINUTE),
//                calendar.get(Calendar.SECOND),
//                StringUtils.leftPad(String.valueOf(calendar.get(Calendar.MILLISECOND)), 3, '0')
//        });
//        System.out.println(mid);
//        System.out.println((System.currentTimeMillis() - start));
    }
}
