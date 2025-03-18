package com.bxm.warcar.integration.eventbus;


import com.bxm.warcar.integration.eventbus.core.EventBus;

/**
 * <p>同步的事件注册、提交中心</p>
 *
 * <p>同步队列，会阻塞发送线程，直到所有的监听器处理完成。</p>
 *
 * <p>如果没有监听者将发送死信事件 DeadEvent
 *
 * @author allen
 * @since 1.0.0
 */
public class SyncEventPark extends AbstractEventPark {

    private final EventBus eventBus = new EventBus();

    @Override
    protected EventBus getEventBus() {
        return eventBus;
    }

    @Override
    protected String getEventParkName() {
        return "SyncEventPark";
    }
}
