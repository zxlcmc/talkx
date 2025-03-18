package org.bigmouth.gpt.integration;

import java.util.concurrent.TimeUnit;

/**
 * @author Allen Hu
 * @date 2025/3/17
 */
public class EmptyDistributedLock implements DistributedLock {
    @Override
    public boolean acquire(String id) {
        return true;
    }

    @Override
    public boolean acquire(String id, long time, TimeUnit unit) {
        return true;
    }

    @Override
    public void release(String id) {
    }
}
