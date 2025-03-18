package org.bigmouth.gpt.xiaozhi.event.listener;

import com.bxm.warcar.integration.eventbus.EventListener;
import com.bxm.warcar.integration.eventbus.core.Subscribe;
import lombok.extern.slf4j.Slf4j;
import org.bigmouth.gpt.xiaozhi.entity.Udp;
import org.bigmouth.gpt.xiaozhi.entity.UdpHello;
import org.bigmouth.gpt.xiaozhi.event.ListenStartEvent;
import org.bigmouth.gpt.xiaozhi.handler.HelloMessageHandler;
import org.bigmouth.gpt.xiaozhi.handler.IotMessageHandler;
import org.bigmouth.gpt.xiaozhi.udp.ClientAddressHolder;
import org.bigmouth.gpt.xiaozhi.udp.UdpClientContext;
import org.bigmouth.gpt.xiaozhi.udp.UdpClientContextHolder;
import org.springframework.context.annotation.Configuration;

/**
 * @author Allen Hu
 * @date 2025/2/25
 */
@Slf4j
@Configuration
public class ListenStartResetContextListener implements EventListener<ListenStartEvent> {

    private final UdpClientContextHolder udpClientContextHolder;
    private final HelloMessageHandler helloMessageHandler;
    private final IotMessageHandler iotMessageHandler;
    private final ClientAddressHolder clientAddressHolder;

    public ListenStartResetContextListener(UdpClientContextHolder udpClientContextHolder, HelloMessageHandler helloMessageHandler, IotMessageHandler iotMessageHandler, ClientAddressHolder clientAddressHolder) {
        this.udpClientContextHolder = udpClientContextHolder;
        this.helloMessageHandler = helloMessageHandler;
        this.iotMessageHandler = iotMessageHandler;
        this.clientAddressHolder = clientAddressHolder;
    }

    @Override
    @Subscribe
    public void consume(ListenStartEvent event) {
        String sessionId = event.getDataPacket().getSessionId();
        UdpClientContext context = udpClientContextHolder.get(sessionId);
        if (context != null) {
            UdpHello udpHello = context.getUdpHello();
            Udp udp = udpHello.getResponse().getUdp();
            String nonce = udp.getNonce();
            String nonceRandomString = HelloMessageHandler.parseNonceRandomString(nonce);
            helloMessageHandler.refreshByNonceRandomString(nonceRandomString);
            helloMessageHandler.refreshBySessionId(sessionId);

            // 音频响应序列号续租
            context.getAudioResponseSequence().renewLease();
        }
        iotMessageHandler.refresh(sessionId);
        clientAddressHolder.refresh(sessionId);
    }
}
