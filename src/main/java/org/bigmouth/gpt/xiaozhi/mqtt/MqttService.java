package org.bigmouth.gpt.xiaozhi.mqtt;

import org.bigmouth.gpt.xiaozhi.OtaResponse;

public interface MqttService {

    MqttType of();

    OtaResponse.Mqtt create(String macAddress);

    void sendMessage(String topic, byte[] payload, MqttSendCallback sendCallback);
}
