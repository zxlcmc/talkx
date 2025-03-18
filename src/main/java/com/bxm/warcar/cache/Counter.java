package com.bxm.warcar.cache;

import java.util.Map;

/**
 * 计数器
 *
 * @author allen
 * @date 2017-12-04
 */
public interface Counter {

    Long incrementAndGet(KeyGenerator keyGenerator);

    Long incrementAndGet(KeyGenerator keyGenerator, int expireTimeInSecond);

    Long incrementByAndGet(KeyGenerator keyGenerator, long inc);

    Long incrementByAndGet(KeyGenerator keyGenerator, long inc, int expireTimeInSecond);

    /**
     * 浮点值的原子操作<br/>
     *
     * @param keyGenerator
     * @param inc
     * @return
     */
    Double incrFloatByAndGet(KeyGenerator keyGenerator, double inc);

    /**
     * 浮点值的原子操作<br/>
     *
     * @param keyGenerator
     * @param inc
     * @param expireTimeInSecond
     * @return
     */
    Double incrFloatByAndGet(KeyGenerator keyGenerator, double inc, int expireTimeInSecond);

    Long decrementAndGet(KeyGenerator keyGenerator);

    Long decrementAndGet(KeyGenerator keyGenerator, int expireTimeInSecond);

    Long decrementByAndGet(KeyGenerator keyGenerator, long dec);

    Long decrementByAndGet(KeyGenerator keyGenerator, long dec, int expireTimeInSecond);

    void set(KeyGenerator keyGenerator, long value);

    /**
     * 重新设置redis key的过期时间<br/>
     *
     * @param keyGenerator
     * @param seconds
     * @return
     */
    void expire(KeyGenerator keyGenerator, int seconds);

    Long get(KeyGenerator keyGenerator);

    Long hincrementAndGet(KeyGenerator keyGenerator, String field);

    Long hincrementAndGet(KeyGenerator keyGenerator, String field, int expireTimeInSecond);

    Long hincrementByAndGet(KeyGenerator keyGenerator, String field, long inc);

    Long hincrementByAndGet(KeyGenerator keyGenerator, String field, long inc, int expireTimeInSecond);

    /**
     * 浮点值的原子操作<br/>
     *
     * @param keyGenerator
     * @param field
     * @param inc
     * @return
     */
    Double hincrFloatByAndGet(KeyGenerator keyGenerator, String field, double inc);

    /**
     * 浮点值的原子操作<br/>
     *
     * @param keyGenerator
     * @param field
     * @param inc
     * @param expireTimeInSecond
     * @return
     */
    Double hincrFloatByAndGet(KeyGenerator keyGenerator, String field, double inc, int expireTimeInSecond);

    Long hget(KeyGenerator keyGenerator, String field);

    Map<String, Long> hgetall(KeyGenerator keyGenerator);

    /**
     * 返回这个获取器具体的实现原生客户端
     * @return
     */
    Object getClientOriginal();
}
