package org.bigmouth.gpt.entity;

/**
 * @author allen
 * @date 2023/6/27
 * @since 1.0.0
 */
public interface RoleType {

    /**
     * 通用的
     */
    String GENERAL = "0";

    /**
     * 中英翻译
     */
    String CN_EN_TRANSLATION = "cn_en_translation";

    /**
     * 写作助理
     */
    String WRITING_ASSISTANT = "writing_assistant";

    /**
     * 编程助手
     */
    String CODING_ASSISTANT = "coding_assistant";

    /**
     * DALL`E
     */
    String DALL_E = "dall_e";
    String DALL_E_3 = "dall-e-3";
    String OPENAI_TTS_1 = "openai-tts-1";
    String OPENAI_WHISPER_1 = "openai-whisper-1";
}
