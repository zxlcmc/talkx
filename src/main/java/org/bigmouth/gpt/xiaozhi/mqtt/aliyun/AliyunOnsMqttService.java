package org.bigmouth.gpt.xiaozhi.mqtt.aliyun;

import com.alibaba.mqtt.server.callback.SendCallback;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bigmouth.gpt.service.IDeviceService;
import org.bigmouth.gpt.service.impl.DeviceServiceImpl;
import org.bigmouth.gpt.xiaozhi.OtaResponse;
import org.bigmouth.gpt.xiaozhi.aliyun.Tools;
import org.bigmouth.gpt.xiaozhi.config.XiaozhiMqttConfig;
import org.bigmouth.gpt.xiaozhi.mqtt.MqttSendCallback;
import org.bigmouth.gpt.xiaozhi.mqtt.MqttService;
import org.bigmouth.gpt.xiaozhi.mqtt.MqttType;
import org.springframework.context.annotation.Configuration;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * @author Allen Hu
 * @date 2025/3/17
 */
@Slf4j
@AllArgsConstructor
public class AliyunOnsMqttService implements MqttService {

    private final XiaozhiMqttConfig xiaozhiMqttConfig;
    private final IDeviceService deviceService;
    private final MqttCloudProducer mqttCloudProducer;

    @Override
    public MqttType of() {
        return MqttType.ALIYUN_ONS;
    }

    @Override
    public OtaResponse.Mqtt create(String macAddress) {
        try {
            OtaResponse.Mqtt mqtt = new OtaResponse.Mqtt();
            mqtt.setEndpoint(xiaozhiMqttConfig.getEndpoint());
            String clientIdSuffix = DeviceServiceImpl.parse2ClientIdSuffix(macAddress);
            String clientId = deviceService.generateClientId(macAddress);
            mqtt.setClientId(clientId);
            mqtt.setUsername("Signature|" + xiaozhiMqttConfig.getAccessKey() + "|" + xiaozhiMqttConfig.getInstanceId());
            mqtt.setPassword(Tools.macSignature(clientId, xiaozhiMqttConfig.getSecretKey()));
            mqtt.setPublishTopic(xiaozhiMqttConfig.getTopicOfServer());
            mqtt.setSubscribeTopic(xiaozhiMqttConfig.getTopicOfDevice() + "/" + clientIdSuffix);
            return mqtt;
        } catch (InvalidKeyException | NoSuchAlgorithmException e) {
            log.error("create mqtt error", e);
            return null;
        }
    }

    @Override
    public void sendMessage(String topic, byte[] payload, MqttSendCallback sendCallback) {
        mqttCloudProducer.sendMessage(topic, payload, new SendCallback() {
            @Override
            public void onSuccess(String msgId) {
                sendCallback.onSuccess(msgId);
            }

            @Override
            public void onFail() {
                sendCallback.onFail();
            }
        });
    }
}
