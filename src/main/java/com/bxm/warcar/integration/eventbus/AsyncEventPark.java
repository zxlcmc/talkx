package com.bxm.warcar.integration.eventbus;

import com.bxm.warcar.integration.eventbus.core.AsyncEventBus;
import com.bxm.warcar.integration.eventbus.core.EventBus;
import com.bxm.warcar.utils.NamedThreadFactory;

import java.util.EventObject;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * <p>异步的事件注册、提交中心</p>
 *
 * <p>异步队列，不会阻塞发送 {@link #post(EventObject)} 线程，生产的消息会先 {@code add} 到 {@code Dispatcher#ConcurrentLinkedQueue} 队列里，然后 {@code pool} 队列，使用线程池处理。
 * <p>消费者执行时会占用线程池队列，声明 @AllowConcurrentEvents 表示这个监听器支持多线程处理，否则同一时间只会处理一条消息。
 *
 * <p>如果没有监听者将发送死信事件 DeadEvent
 *
 * @author allen
 * @since V1.0.0 2017/12/15
 */
public final class AsyncEventPark extends AbstractEventPark {

    private final AsyncEventBus asyncEventBus;
    private final ThreadPoolExecutor threadPoolExecutor;

    public AsyncEventPark() {
        this(Runtime.getRuntime().availableProcessors() * 2 + 1);
    }

    public AsyncEventPark(int coreSize) {
        this(new ThreadPoolExecutor(coreSize, coreSize, 0, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(), new NamedThreadFactory("event-park")));
    }

    public AsyncEventPark(ThreadPoolExecutor executor) {
        this.threadPoolExecutor = executor;
        this.asyncEventBus = new AsyncEventBus("async-event-bus", executor);
    }

    @Override
    protected EventBus getEventBus() {
        return asyncEventBus;
    }

    @Override
    protected String getEventParkName() {
        return "AsyncEventPark";
    }

    public int getCorePoolSize() {
        return this.threadPoolExecutor.getCorePoolSize();
    }

    public int getActiveCount() {
        return this.threadPoolExecutor.getActiveCount();
    }

    public int getQueueSize() {
        return this.threadPoolExecutor.getQueue().size();
    }
}
