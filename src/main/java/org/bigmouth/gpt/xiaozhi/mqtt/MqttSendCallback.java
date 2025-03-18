package org.bigmouth.gpt.xiaozhi.mqtt;

public interface MqttSendCallback {
    void onSuccess(String msgId);

    void onFail();
}
