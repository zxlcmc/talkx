package org.bigmouth.gpt.xiaozhi.forest;

import com.dtflys.forest.annotation.BaseRequest;
import com.dtflys.forest.annotation.JSONBody;
import com.dtflys.forest.annotation.Post;
import org.bigmouth.gpt.xiaozhi.OtaResponse;
import org.bigmouth.gpt.xiaozhi.entity.asr.TalkXAsrRequest;

@BaseRequest(baseURL = "${talkxCenterBaseUrl}")
public interface TalkXApi {

    @Post("/xiaozhi/asr")
    String asr(@JSONBody TalkXAsrRequest request);

    @Post("/xiaozhi/tts")
    byte[] tts(@JSONBody TtsRequest request);

    @Post("/xiaozhi/ota/mqtt")
    OtaResponse.Mqtt create(@JSONBody OtaMqttRequest request);

    @Post("/xiaozhi/ota/mqtt_forward_connect")
    ForwardConnectResponse forwardConnect(@JSONBody ForwardConnectRequest request);
}
