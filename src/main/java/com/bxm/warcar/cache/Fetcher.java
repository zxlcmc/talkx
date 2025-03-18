package com.bxm.warcar.cache;

import org.apache.commons.collections.keyvalue.DefaultKeyValue;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 获取器
 * @author allen hu
 */
public interface Fetcher {

    /**
     * 获取一个对象
     * @param keyGenerator
     * @param cls
     * @param <T>
     * @return
     */
    <T> T fetch(KeyGenerator keyGenerator, Class<T> cls);

    <T> T fetch(KeyGenerator keyGenerator, DataExtractor<T> dataExtractor, Class<T> cls);

    <T> T fetch(KeyGenerator keyGenerator, Class<T> cls, int expireTimeInSecond);

    <T> T fetch(KeyGenerator keyGenerator, DataExtractor<T> dataExtractor, Class<T> cls, int expireTimeInSecond);

    <T> List<T> fetchList(KeyGenerator keyGenerator, Class<T> cls);

    <T> List<T> fetchList(KeyGenerator keyGenerator, DataExtractor<List<T>> dataExtractor, Class<T> cls);

    <T> List<T> fetchList(KeyGenerator keyGenerator, Class<T> cls, int expireTimeInSecond);

    <T> List<T> fetchList(KeyGenerator keyGenerator, DataExtractor<List<T>> dataExtractor, Class<T> cls, int expireTimeInSecond);

    <T> T hfetch(KeyGenerator keyGenerator, String field, Class<T> cls);

    <T> T hfetch(KeyGenerator keyGenerator, String field, DataExtractor<T> dataExtractor, Class<T> cls);

    <T> T hfetch(KeyGenerator keyGenerator, String field, Class<T> cls, int expireTimeInSecond);

    <T> T hfetch(KeyGenerator keyGenerator, String field, DataExtractor<T> dataExtractor, Class<T> cls, int expireTimeInSecond);

    <T> List<T> hfetchList(KeyGenerator keyGenerator, String field, Class<T> cls);

    <T> List<T> hfetchList(KeyGenerator keyGenerator, String field, DataExtractor<List<T>> dataExtractor, Class<T> cls);

    <T> List<T> hfetchList(KeyGenerator keyGenerator, String field, Class<T> cls, int expireTimeInSecond);

    <T> List<T> hfetchList(KeyGenerator keyGenerator, String field, DataExtractor<List<T>> dataExtractor, Class<T> cls, int expireTimeInSecond);

    <T> Map<String, T> hfetchall(KeyGenerator keyGenerator, Class<T> cls);

    <T> Map<String, T> hfetchall(KeyGenerator keyGenerator, DataExtractor<Map<String, T>> dataExtractor, Class<T> cls);

    <T> Map<String, T> hfetchall(KeyGenerator keyGenerator, Class<T> cls, int expireTimeInSecond);

    <T> Map<String, T> hfetchall(KeyGenerator keyGenerator, DataExtractor<Map<String, T>> dataExtractor, Class<T> cls, int expireTimeInSecond);

    <T> T fetchWithSelector(KeyGenerator keyGenerator, Class<T> cls, int selector);

    <T> T fetchWithSelector(KeyGenerator keyGenerator, DataExtractor<T> dataExtractor, Class<T> cls, int selector);

    <T> T fetchWithSelector(KeyGenerator keyGenerator, Class<T> cls, int expireTimeInSecond, int selector);

    <T> T fetchWithSelector(KeyGenerator keyGenerator, DataExtractor<T> dataExtractor, Class<T> cls, int expireTimeInSecond, int selector);

    <T> List<T> fetchListWithSelector(KeyGenerator keyGenerator, Class<T> cls, int selector);

    <T> List<T> fetchListWithSelector(KeyGenerator keyGenerator, DataExtractor<List<T>> dataExtractor, Class<T> cls, int selector);

    <T> List<T> fetchListWithSelector(KeyGenerator keyGenerator, Class<T> cls, int expireTimeInSecond, int selector);

    <T> List<T> fetchListWithSelector(KeyGenerator keyGenerator, DataExtractor<List<T>> dataExtractor, Class<T> cls, int expireTimeInSecond, int selector);

    <T> T hfetchWithSelector(KeyGenerator keyGenerator, String field, Class<T> cls, int selector);

    <T> T hfetchWithSelector(KeyGenerator keyGenerator, String field, DataExtractor<T> dataExtractor, Class<T> cls, int selector);

    <T> T hfetchWithSelector(KeyGenerator keyGenerator, String field, Class<T> cls, int expireTimeInSecond, int selector);

    <T> T hfetchWithSelector(KeyGenerator keyGenerator, String field, DataExtractor<T> dataExtractor, Class<T> cls, int expireTimeInSecond, int selector);

    <T> List<T> hfetchListWithSelector(KeyGenerator keyGenerator, String field, Class<T> cls, int selector);

    <T> List<T> hfetchListWithSelector(KeyGenerator keyGenerator, String field, DataExtractor<List<T>> dataExtractor, Class<T> cls, int selector);

    <T> List<T> hfetchListWithSelector(KeyGenerator keyGenerator, String field, Class<T> cls, int expireTimeInSecond, int selector);

    <T> List<T> hfetchListWithSelector(KeyGenerator keyGenerator, String field, DataExtractor<List<T>> dataExtractor, Class<T> cls, int expireTimeInSecond, int selector);

    <T> Map<String, T> hfetchallWithSelector(KeyGenerator keyGenerator, Class<T> cls, int selector);

    <T> Map<String, T> hfetchallWithSelector(KeyGenerator keyGenerator, DataExtractor<Map<String, T>> dataExtractor, Class<T> cls, int selector);

    <T> Map<String, T> hfetchallWithSelector(KeyGenerator keyGenerator, Class<T> cls, int expireTimeInSecond, int selector);

    <T> Map<String, T> hfetchallWithSelector(KeyGenerator keyGenerator, DataExtractor<Map<String, T>> dataExtractor, Class<T> cls, int expireTimeInSecond, int selector);

    /**
     * 统计HyperLogLog类型key数量
     * @param keyGenerator
     * @return
     */
    Long pfCount(KeyGenerator keyGenerator);

    Long pfCount(KeyGenerator keyGenerator, int expireTimeInSecond);

    Long pfCountWithSelector(KeyGenerator keyGenerator,int selector);

    Long pfCountWithSelector(KeyGenerator keyGenerator, int expireTimeInSecond,int selector);

    /**
     * zscan 返回member列表带排序
     * @param keyGenerator
     * @param cls
     * @param <T>
     * @return
     */
    <T> List<T> zfetchList(KeyGenerator keyGenerator,  Class<T> cls);

    <T> List<T> zfetchList(KeyGenerator keyGenerator,  Class<T> cls, int expireTimeInSecond);

    <T> List<T> zfetchListWithSelector(KeyGenerator keyGenerator,  Class<T> cls, int selector);

    /**
     * 只返回member列表
     * @param keyGenerator
     * @param cls
     * @param expireTimeInSecond
     * @param selector
     * @param <T>
     * @return
     */
    <T> List<T> zfetchListWithSelector(KeyGenerator keyGenerator, Class<T> cls, int expireTimeInSecond, int selector);

    /**
     * 返回member和score
     * @param keyGenerator
     * @param cls
     * @param <T>
     * @return
     */
    <T> List<DefaultKeyValue> zfetchAll(KeyGenerator keyGenerator, Class<T> cls);

    <T> List<DefaultKeyValue> zfetchAll(KeyGenerator keyGenerator, Class<T> cls, int expireTimeInSecond);

    <T> List<DefaultKeyValue> zfetchAllWithSelector(KeyGenerator keyGenerator, Class<T> cls, int selector);

    /**
     * 返回member和score
     * @param keyGenerator
     * @param cls
     * @param expireTimeInSecond
     * @param selector
     * @param <T>
     * @return
     */
    <T> List<DefaultKeyValue> zfetchAllWithSelector(KeyGenerator keyGenerator, Class<T> cls, int expireTimeInSecond, int selector);

    boolean exists(KeyGenerator keyGenerator);

    boolean existsWithSelector(KeyGenerator keyGenerator, int selector);

    <T> Set<T> sfetchAll(KeyGenerator keyGenerator, Class<T> cls);
    <T> Set<T> sfetchAll(KeyGenerator keyGenerator, Class<T> cls, int selector);

    /**
     * 返回这个获取器具体的实现原生客户端
     * @return
     */
    Object getClientOriginal();

    Long ttl(KeyGenerator keyGenerator);

    Long ttl(KeyGenerator keyGenerator, int selector);
}
