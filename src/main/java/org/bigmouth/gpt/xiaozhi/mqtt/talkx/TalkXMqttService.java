package org.bigmouth.gpt.xiaozhi.mqtt.talkx;

import com.bxm.warcar.utils.StringHelper;
import com.bxm.warcar.utils.UUIDHelper;
import lombok.AllArgsConstructor;
import org.bigmouth.gpt.xiaozhi.OtaResponse;
import org.bigmouth.gpt.xiaozhi.config.XiaozhiMqttConfig;
import org.bigmouth.gpt.xiaozhi.forest.OtaMqttRequest;
import org.bigmouth.gpt.xiaozhi.forest.TalkXApi;
import org.bigmouth.gpt.xiaozhi.mqtt.MqttSendCallback;
import org.bigmouth.gpt.xiaozhi.mqtt.MqttService;
import org.bigmouth.gpt.xiaozhi.mqtt.MqttType;

/**
 * @author Allen Hu
 * @date 2025/3/17
 */
@AllArgsConstructor
public class TalkXMqttService implements MqttService {

    private final XiaozhiMqttConfig xiaozhiMqttConfig;
    private final TalkXMqttFrontEndClient talkXMqttFrontEndClient;
    private final TalkXApi talkXApi;

    @Override
    public MqttType of() {
        return MqttType.TALKX;
    }

    @Override
    public OtaResponse.Mqtt create(String macAddress) {
        OtaMqttRequest request = new OtaMqttRequest();
        request.setClientMacAddress(macAddress);
        request.setFrontEndClientId(talkXMqttFrontEndClient.getClientId());
        return talkXApi.create(request);
    }

    @Override
    public void sendMessage(String topic, byte[] payload, MqttSendCallback sendCallback) {
        ForwardMessage message = new ForwardMessage();
        message.setId(UUIDHelper.generate());
        message.setAction(ForwardMessage.ACTION_SEND_MSG);
        message.setTopic(topic);
        message.setJson(StringHelper.convert(payload));
        talkXMqttFrontEndClient.publish(xiaozhiMqttConfig.getTalkxMqttTopicOfServer(), message);
    }
}
