package com.bxm.warcar.cache;

public interface DataExtractor<T> {

    T extract();

    /**
     * 更新并返回缓存过期时间，单位秒。
     *
     * 一般情况在方法传入的 expireTimeInSecond 参数后，希望在获取原始数据后修改过期时间。
     * 可以重载这个方法，返回新的过期时间。
     *
     * @param extract {@link #extract()} 方法执行后的结果
     * @param expireTimeInSecond 方法传入的过期时间，单位秒。
     * @return 新的过期时间
     */
    default int updateExpireTimeInSecond(T extract, int expireTimeInSecond) {
        return expireTimeInSecond;
    }
}
