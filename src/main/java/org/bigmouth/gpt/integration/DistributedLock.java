package org.bigmouth.gpt.integration;

import java.util.concurrent.TimeUnit;

/**
 * Lock for distributed
 * @author allen
 * @date 2023-11-03
 * @since 1.0
 */
public interface DistributedLock {

    /**
     * Acquire the mutex - blocking until it's available. Each call to acquire must be balanced by a call
     * to {@link #release(String)}
     * @param id id
     * @return true if the mutex was acquired, false if not
     *
     */
    boolean acquire(String id);

    /**
     * Acquire the mutex - blocks until it's available or the given time expires. Each call to acquire that returns true must be balanced by a call
     * to {@link #release(String)}
     *
     * @param id id
     * @param time time to wait
     * @param unit time unit
     * @return true if the mutex was acquired, false if not
     */
    boolean acquire(String id, long time, TimeUnit unit);

    /**
     * Perform one release of the mutex.
     *
     */
    void release(String id);
}
