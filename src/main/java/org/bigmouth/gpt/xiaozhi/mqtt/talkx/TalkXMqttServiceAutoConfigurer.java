package org.bigmouth.gpt.xiaozhi.mqtt.talkx;

import org.bigmouth.gpt.xiaozhi.autoconfigure.XiaozhiAutoConfigurer;
import org.bigmouth.gpt.xiaozhi.config.XiaozhiMqttConfig;
import org.bigmouth.gpt.xiaozhi.forest.TalkXApi;
import org.bigmouth.gpt.xiaozhi.handler.MessageHandlerFactory;
import org.bigmouth.gpt.xiaozhi.mqtt.MqttService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Allen Hu
 * @date 2025/3/17
 */
@Configuration
@ConditionalOnBean(XiaozhiAutoConfigurer.class)
@ConditionalOnProperty(name = "xiaozhi.mqtt.type", havingValue = "talkx")
public class TalkXMqttServiceAutoConfigurer {

    private final XiaozhiMqttConfig xiaozhiMqttConfig;
    private final TalkXApi talkXApi;
    private final MessageHandlerFactory messageHandlerFactory;

    public TalkXMqttServiceAutoConfigurer(XiaozhiMqttConfig xiaozhiMqttConfig, TalkXApi talkXApi, MessageHandlerFactory messageHandlerFactory) {
        this.xiaozhiMqttConfig = xiaozhiMqttConfig;
        this.talkXApi = talkXApi;
        this.messageHandlerFactory = messageHandlerFactory;
    }

    @Bean
    @ConditionalOnMissingBean(MqttService.class)
    public MqttService talkXMqttService() {
        TalkXMqttFrontEndClient talkXMqttFrontEndClient = talkXMqttForwardClient();
        return new TalkXMqttService(xiaozhiMqttConfig, talkXMqttFrontEndClient, talkXApi);
    }

    @Bean
    public TalkXMqttFrontEndClient talkXMqttForwardClient() {
        return new TalkXMqttFrontEndClient(talkXApi, messageHandlerFactory, xiaozhiMqttConfig);
    }
}
