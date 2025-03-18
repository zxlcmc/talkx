package com.bxm.warcar.id.random;

import com.bxm.warcar.id.IdGenerator;
import com.bxm.warcar.id.IdUtils;
import com.bxm.warcar.utils.NamedThreadFactory;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.RandomUtils;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 随机数字生成器。
 * 理论上有可能重复，但是在默认无前缀、32 位长度下，在同一毫秒内 15 (0-999999999999999) 个数字随机出现一致的可能性非常低。
 *
 * @author allen
 * @date 2022-03-22
 * @since 1.0
 */
public class RandomIdGenerator implements IdGenerator {

    private static final int DEFAULT_LEN = 32;

    /**
     * ID 长度
     */
    private int len;
    private final String prefix;

    public RandomIdGenerator() {
        this(null);
    }

    public RandomIdGenerator(String prefix) {
        this(DEFAULT_LEN, prefix);
    }

    public RandomIdGenerator(int len, String prefix) {
        this.len = len;
        this.prefix = prefix;
    }

    @Override
    public String next() {
        String mid = getDateTime();
        int midLen = StringUtils.length(mid) + StringUtils.length(prefix);
        int i = len - midLen;
        StringBuilder s = new StringBuilder();
        for (int j = 0; j < i; j++) {
            s.append("9");
        }
        long value = RandomUtils.nextLong(0, NumberUtils.toLong(s.toString()));
        return StringUtils.defaultIfBlank(prefix, "") + mid + StringUtils.leftPad(StringUtils.right(String.valueOf(value), i), i, '0');
    }

    private String getDateTime() {
        return IdUtils.getDateTime();
    }

    public static void main(String[] args) {
        RandomIdGenerator idGenerator = new RandomIdGenerator();
        long start = System.currentTimeMillis();
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(100, 100,
                0, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(), new NamedThreadFactory("pool"));
        int len = 100000;
        CountDownLatch latch = new CountDownLatch(len);
        for (int i = 0; i < len; i++) {
            threadPoolExecutor.execute(() -> {
                idGenerator.next();
                latch.countDown();
            });
        }
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println((System.currentTimeMillis()) - start);
        // multiple Thread consume average 800 millis
    }
}
