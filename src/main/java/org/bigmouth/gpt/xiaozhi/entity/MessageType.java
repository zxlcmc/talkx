package org.bigmouth.gpt.xiaozhi.entity;

/**
 * 消息类型枚举
 * @author Allen Hu
 * @date 2025/2/20
 */
public enum MessageType {
    HELLO("hello"),
    IOT("iot"),
    LISTEN("listen"),
    STT("stt"),
    TTS("tts"),
    LLM("llm"),
    ABORT("abort"),
    GOODBYE("goodbye");

    private final String value;

    MessageType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static MessageType fromValue(String value) {
        for (MessageType type : values()) {
            if (type.value.equals(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown MessageType value: " + value);
    }
}