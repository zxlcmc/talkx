package org.bigmouth.gpt.xiaozhi.mqtt.unsupported;

import org.bigmouth.gpt.xiaozhi.OtaResponse;
import org.bigmouth.gpt.xiaozhi.mqtt.MqttSendCallback;
import org.bigmouth.gpt.xiaozhi.mqtt.MqttService;
import org.bigmouth.gpt.xiaozhi.mqtt.MqttType;

/**
 * 没有开启xiaozhi 的情况下，创建一个空的mqtt服务，避免其他依赖错误。
 *
 * @author Allen Hu
 * @date 2025/3/21
 */
public class UnsupportedMqttService implements MqttService {

    @Override
    public MqttType of() {
        return MqttType.ABSENT;
    }

    @Override
    public OtaResponse.Mqtt create(String macAddress) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void sendMessage(String topic, byte[] payload, MqttSendCallback sendCallback) {
        throw new UnsupportedOperationException();
    }
}
