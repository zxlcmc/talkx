package org.bigmouth.gpt.xiaozhi.event.listener;

import com.bxm.warcar.integration.eventbus.EventListener;
import com.bxm.warcar.integration.eventbus.core.Subscribe;
import org.bigmouth.gpt.xiaozhi.event.VadStartEvent;
import org.springframework.context.annotation.Configuration;

/**
 * @author Allen Hu
 * @date 2025/2/25
 */
@Configuration
public class VadStartClearListener implements EventListener<VadStartEvent> {
    @Override
    @Subscribe
    public void consume(VadStartEvent event) {
        event.getContext().clearVadListeningTimeoutClock();
    }
}
