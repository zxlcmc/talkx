package org.bigmouth.gpt.xiaozhi.mqtt.aliyun;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.mqtt.server.callback.MessageListener;
import com.alibaba.mqtt.server.callback.SendCallback;
import com.alibaba.mqtt.server.model.MessageProperties;
import com.bxm.warcar.utils.JsonHelper;
import lombok.extern.slf4j.Slf4j;
import org.bigmouth.gpt.xiaozhi.config.XiaozhiMqttConfig;
import org.bigmouth.gpt.xiaozhi.entity.DataPacket;
import org.bigmouth.gpt.xiaozhi.entity.MessageType;
import org.bigmouth.gpt.xiaozhi.handler.MessageHandler;
import org.bigmouth.gpt.xiaozhi.handler.MessageHandlerFactory;

/**
 * @author Allen Hu
 * @date 2025/2/21
 */
@Slf4j
public class MqttCloudConsumerMessageListener implements MessageListener {

    private final XiaozhiMqttConfig xiaozhiMqttConfig;
    private final MessageHandlerFactory messageHandlerFactory;
    private final MqttCloudProducer mqttCloudProducer;

    public MqttCloudConsumerMessageListener(XiaozhiMqttConfig xiaozhiMqttConfig,
                                            MessageHandlerFactory messageHandlerFactory,
                                            MqttCloudProducer mqttCloudProducer) {
        this.xiaozhiMqttConfig = xiaozhiMqttConfig;
        this.messageHandlerFactory = messageHandlerFactory;
        this.mqttCloudProducer = mqttCloudProducer;
    }

    @Override
    public void process(String msgId, MessageProperties messageProperties, byte[] payload) {
        String json = new String(payload);
        String firstTopic = messageProperties.getFirstTopic();
        String secondTopic = messageProperties.getSecondTopic();
        String clientId = messageProperties.getClientId();

        log.info("[{}/{}] Receive message: {} from: {}", firstTopic, secondTopic, json, clientId);

        try {
            DataPacket dataPacket = JSONObject.parseObject(json, DataPacket.class);
            MessageType messageType = dataPacket.of();
            MessageHandler messageHandler = messageHandlerFactory.get(messageType);
            if (messageHandler == null) {
                log.warn("No message handler found for message type: {}", messageType);
                return;
            }
            String p2pTopic = createP2pTopic(clientId);
            dataPacket.setClientId(clientId);
            dataPacket.setP2pTopic(p2pTopic);

            DataPacket response = messageHandler.handle(dataPacket);
            if (response == null) {
                return;
            }

            mqttCloudProducer.sendMessage(p2pTopic, JsonHelper.convert2bytes(response), new SendCallback() {
                @Override
                public void onSuccess(String msgId) {
                }

                @Override
                public void onFail() {
                    log.error("Failed to send message: {} - {}", p2pTopic, response);
                }
            });
        } catch (IllegalArgumentException | JSONException e) {
            log.error("Failed to parse message: {}", json, e);
        } catch (Exception e) {
            log.error("Failed to process message: {}", json, e);
        }
    }

    public String createP2pTopic(String clientId) {
        // devices/p2p/GID_test@@@a0_85_e3_e1_55_34
        String topicOfDevice = xiaozhiMqttConfig.getTopicOfDevice();
        return String.format("%s/p2p/%s", topicOfDevice, clientId);
    }
}
