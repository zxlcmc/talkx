package org.bigmouth.gpt.xiaozhi.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * @author Allen Hu
 * @date 2025/2/20
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DataPacket {

    public static final String STATE_START = "start";
    public static final String STATE_DETECT = "detect";
    public static final String STATE_SENTENCE_START = "sentence_start";
    public static final String STATE_STOP = "stop";

    public static final String MODE_AUTO = "auto";
    public static final String MODE_MANUAL = "manual";

    private String type;
    private Integer version;

    @JSONField(name = "session_id")
    private String sessionId;

    private String state;
    private String mode;
    private String text;
    private String emotion;

    // --- hello --- //
    private String transport;
    @JSONField(name = "audio_params")
    private AudioParams audioParams;
    private Udp udp;

    // --- IoT --- //
    private List<Descriptor> descriptors;
    private List<State> states;

    // --- 只有当客户端发送MQTT消息，经过Cloud处理后才会填充这个值 --- //
    private String clientId;
    private String p2pTopic;

    @JSONField(serialize = false, deserialize = false)
    public MessageType of() throws IllegalArgumentException {
        return MessageType.fromValue(type);
    }

    @JSONField(serialize = false, deserialize = false)
    public boolean isStateStart() {
        return STATE_START.equals(state);
    }

    @JSONField(serialize = false, deserialize = false)
    public boolean isStateStop() {
        return STATE_STOP.equals(state);
    }

    @JSONField(serialize = false, deserialize = false)
    public boolean isStateDetect() {
        return STATE_DETECT.equals(state);
    }

    @JSONField(serialize = false, deserialize = false)
    public boolean isModeAuto() {
        return MODE_AUTO.equals(mode);
    }

    @JSONField(serialize = false, deserialize = false)
    public boolean isModeManual() {
        return MODE_MANUAL.equals(mode);
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Descriptor {
        private String name;
        private String description;
        private Map<String, Property> properties;
        private Map<String, Method> methods;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Property {
        private String description;
        private String type;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Method {
        private String description;
        private Map<String, Parameter> parameters;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Parameter {
        private String description;
        private String type;
    }


    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class State {
        private String name;
        private Map<String, Object> state;
    }
}
