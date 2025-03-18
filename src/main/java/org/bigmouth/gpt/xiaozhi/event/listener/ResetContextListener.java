package org.bigmouth.gpt.xiaozhi.event.listener;

import com.bxm.warcar.integration.eventbus.EventListener;
import com.bxm.warcar.integration.eventbus.core.Subscribe;
import org.bigmouth.gpt.xiaozhi.entity.UdpHello;
import org.bigmouth.gpt.xiaozhi.event.ChatCompletionEndEvent;
import org.bigmouth.gpt.xiaozhi.udp.UdpClientContext;
import org.bigmouth.gpt.xiaozhi.udp.UdpClientContextHolder;
import org.springframework.context.annotation.Configuration;

/**
 * @author Allen Hu
 * @date 2025/2/26
 */
@Configuration
public class ResetContextListener implements EventListener<ChatCompletionEndEvent> {

    private final UdpClientContextHolder udpClientContextHolder;

    public ResetContextListener(UdpClientContextHolder udpClientContextHolder) {
        this.udpClientContextHolder = udpClientContextHolder;
    }

    @Override
    @Subscribe
    public void consume(ChatCompletionEndEvent event) {
        UdpHello udpHello = event.getUdpHello();
        UdpClientContext context = udpClientContextHolder.get(udpHello.getSessionId());
        if (context != null) {
            context.reset();
        }
    }
}
