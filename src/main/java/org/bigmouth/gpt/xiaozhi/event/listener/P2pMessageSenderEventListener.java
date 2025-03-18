package org.bigmouth.gpt.xiaozhi.event.listener;


import com.bxm.warcar.integration.eventbus.EventListener;
import com.bxm.warcar.integration.eventbus.core.AllowConcurrentEvents;
import com.bxm.warcar.integration.eventbus.core.Subscribe;
import com.bxm.warcar.utils.StringHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.bigmouth.gpt.xiaozhi.entity.DataPacket;
import org.bigmouth.gpt.xiaozhi.entity.MessageType;
import org.bigmouth.gpt.xiaozhi.entity.UdpHello;
import org.bigmouth.gpt.xiaozhi.event.P2pMessageEvent;
import org.bigmouth.gpt.xiaozhi.mqtt.MqttSendCallback;
import org.bigmouth.gpt.xiaozhi.mqtt.MqttService;
import org.bigmouth.gpt.xiaozhi.udp.UdpClientContext;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;

/**
 * @author Allen Hu
 * @date 2025/2/25
 */
@Slf4j
@Configuration
public class P2pMessageSenderEventListener implements EventListener<P2pMessageEvent> {
    private final MqttService mqttService;

    public P2pMessageSenderEventListener(MqttService mqttService) {
        this.mqttService = mqttService;
    }

    @Override
    @Subscribe
    @AllowConcurrentEvents
    public void consume(P2pMessageEvent event) {
        UdpClientContext context = event.getContext();
        UdpHello udpHello = context.getUdpHello();
        String messageText = event.getMessageText();
        String p2pTopic = udpHello.getRequest().getP2pTopic();

        DataPacket message = event.getMessage();
        if (null != message) {
            boolean isSTT = StringUtils.equals(message.getType(), MessageType.STT.getValue());
            boolean isTTS = StringUtils.equals(message.getType(), MessageType.TTS.getValue());
            boolean isStateStop = message.isStateStop();
            LocalDateTime now = LocalDateTime.now();
            if (isSTT) {
                context.setLastTimeOnStt(now);
            }
            if (isTTS && isStateStop) {
                context.setLastTimeOnTtsStop(now);
            }
        }

        // 发送MQTT消息
        mqttService.sendMessage(p2pTopic, StringHelper.convert(messageText), new MqttSendCallback() {
            @Override
            public void onSuccess(String msgId) {
                log.info("{} MQTT -> {}", udpHello.getSessionId(), messageText);
            }

            @Override
            public void onFail() {
                log.error("{} send message fail!", p2pTopic);
            }
        });
    }
}
