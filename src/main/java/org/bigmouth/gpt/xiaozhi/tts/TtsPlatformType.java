package org.bigmouth.gpt.xiaozhi.tts;

public enum TtsPlatformType {

    Alibaba,

    ByteDance,

    TalkX;

    public static TtsPlatformType of(String type) {
        for (TtsPlatformType t : values()) {
            if (t.name().equalsIgnoreCase(type)) {
                return t;
            }
        }
        throw new IllegalArgumentException("Invalid tts platform type: " + type);
    }

    public static TtsPlatformType of(String type, TtsPlatformType defaultType) {
        try {
            return of(type);
        } catch (IllegalArgumentException e) {
            return defaultType;
        }
    }
}
