package com.bxm.warcar.cache;

import java.util.Map;

/**
 * 更新器
 */
public interface Updater {

    /**
     * 重新设置redis key的过期时间<br/>
     *
     * @param keyGenerator
     * @param seconds
     * @return
     */
    void expire(KeyGenerator keyGenerator, int seconds);

    void update(KeyGenerator keyGenerator, Object value);

    void update(KeyGenerator keyGenerator, Object value, int expireTimeInSecond);

    void updateWithSelector(KeyGenerator keyGenerator, Object value, int selector);

    void updateWithSelector(KeyGenerator keyGenerator, Object value, int expireTimeInSecond, int selector);

    void remove(KeyGenerator keyGenerator);

    void removeWithSelector(KeyGenerator keyGenerator, int selector);

    void hupdate(KeyGenerator keyGenerator, String field, Object value);

    void hupdate(KeyGenerator keyGenerator, String field, Object value, int expireTimeInSecond);

    void hupdateWithSelector(KeyGenerator keyGenerator, String field, Object value, int selector);

    void hupdateWithSelector(KeyGenerator keyGenerator, String field, Object value, int expireTimeInSecond, int selector);

    void hremove(KeyGenerator keyGenerator, String...field);

    void hremoveWithSelector(KeyGenerator keyGenerator, int selector, String...field);

    /**
     * 批量更新 hash 数据。注意数据是不会再经过序列化的，传入前请自行序列化。
     * @param keyGenerator
     * @param hash
     */
    void hmupdate(KeyGenerator keyGenerator, Map<String, String> hash);

    void hmupdateWithSelector(KeyGenerator keyGenerator, Map<String, String> hash, int selector);

    /**
     * 批量更新 hash 数据。注意数据是不会再经过序列化的，传入前请自行序列化。
     *
     * @param keyGenerator
     * @param hash
     * @param expireTimeInSecond
     */
    void hmupdate(KeyGenerator keyGenerator, Map<String, String> hash, int expireTimeInSecond);

    void hmupdateWithSelector(KeyGenerator keyGenerator, Map<String, String> hash, int expireTimeInSecond, int selector);

    /**
     * SortedSet（有序集合）zadd 实现
     * 有序集合类型新增或更新数据
     * @param keyGenerator
     * @param score 分数
     * @param member 元素
     */
    void zupdate(KeyGenerator keyGenerator,Double score,Object member);

    /**
     * SortedSet（有序集合）zadd实现带失效时间
     * @param keyGenerator
     * @param score
     * @param member
     * @param expireTimeInSecond 失效时间
     */
    void zupdate(KeyGenerator keyGenerator,Double score,Object member, int expireTimeInSecond);

    void zupdateWithSelector(KeyGenerator keyGenerator,Double score,Object member, int selector);

    void zupdateWithSelector(KeyGenerator keyGenerator,Double score,Object member, int expireTimeInSecond, int selector);

    void zmupdate(KeyGenerator keyGenerator, Map<String, Double> hash);

    void zmupdate(KeyGenerator keyGenerator, Map<String, Double> hash, int expireTimeInSecond);

    void zmupdateWithSelector(KeyGenerator keyGenerator, Map<String, Double> hash, int selector);

    void zmupdateWithSelector(KeyGenerator keyGenerator, Map<String, Double> hash, int expireTimeInSecond, int selector);

    void zremove(KeyGenerator keyGenerator, String...field);

    void zremoveWithSelector(KeyGenerator keyGenerator, int selector, String...field);

    /**
     * Set（集合）sadd实现
     * @param keyGenerator
     * @param member
     */
    void supdate(KeyGenerator keyGenerator,String... member);

    void supdate(KeyGenerator keyGenerator, int expireTimeInSecond,String... member);

    void supdateWithSelector(KeyGenerator keyGenerator , int selector,String... member);

    void supdateWithSelector(KeyGenerator keyGenerator, int expireTimeInSecond, int selector,String... member);

    void srem(KeyGenerator keyGenerator,String... member);

    void sremWithSelector(KeyGenerator keyGenerator, int selector,String... member);

    /**
     * 返回这个获取器具体的实现原生客户端
     * @return
     */
    Object getClientOriginal();
}
