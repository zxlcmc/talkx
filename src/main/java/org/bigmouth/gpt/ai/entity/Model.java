package org.bigmouth.gpt.ai.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author allen
 * @date 2023/4/28
 * @since 1.0.0
 */
@Getter
@AllArgsConstructor
public enum Model {
    /**
     * gpt-3.5-turbo
     */
    GPT_3_5_TURBO("gpt-3.5-turbo"),
    /**
     * gpt-3.5-turbo-0613
     */
    @Deprecated
    GPT_3_5_TURBO_0613("gpt-3.5-turbo-0613"),
    /**
     * gpt-3.5-turbo-16K
     */
    GPT_3_5_TURBO_16K("gpt-3.5-turbo-16k"),
    /**
     * gpt-3.5-turbo-16k-0613
     */
    @Deprecated
    GPT_3_5_TURBO_16K_0613("gpt-3.5-turbo-16k-0613"),
    /**
     * gpt-3.5-turbo-1106
     */
    GPT_3_5_TURBO_1106("gpt-3.5-turbo-1106"),
    /**
     * gpt-3.5-turbo-instruct
     */
    GPT_3_5_TURBO_INSTRUCT("gpt-3.5-turbo-instruct"),
    /**
     * GPT4.0
     */
    GPT_4("gpt-4"),
    /**
     * gpt-4-0613
     */
    GPT_4_0613("gpt-4-0613"),
    /**
     * GPT4.0 超长上下文
     */
    GPT_4_32K("gpt-4-32k"),
    /**
     * gpt-4-32k-0613
     */
    GPT_4_32K_0613("gpt-4-32k-0613"),
    /**
     * gpt-4-1106-preview
     */
    GPT_4_1106_PREVIEW("gpt-4-1106-preview"),
    /**
     * gpt-4-vision-preview
     */
    GPT_4_VISION_PREVIEW("gpt-4-vision-preview"),

    /**
     * gpt-4o
     */
    GPT_4O("gpt-4o"),

    GPT_4_ALL("gpt-4-all"),

    GPT_4_GIZMO("gpt-4-gizmo-*"),

    GPT_4O_MINI("gpt-4o-mini"),

    O1("o1"),
    O1_MINI("o1-mini"),
    O1_PREVIEW("o1-preview")
    ;
    /**
     * model name
     */
    private final String name;

    public static Model ofName(String name) {
        for (Model model : values()) {
            if (model.getName().equals(name)) {
                return model;
            }
        }
        return null;
    }

    public static Integer getOutputMaxTokens(String name) {
        Model model = ofName(name);
        if (null == model) {
            return null;
        }
        switch (model) {
            case GPT_3_5_TURBO_1106:
            case GPT_4_1106_PREVIEW:
            case GPT_4_VISION_PREVIEW:
                return 4096;
            default:
                return null;
        }
    }
}
