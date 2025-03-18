package org.bigmouth.gpt.ai.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * @author Allen Hu
 * @date 2024/12/24
 */
@Data
public class Usage {

    //    "usage": {
    //        "prompt_tokens": 10,
    //        "completion_tokens": 18,
    //        "total_tokens": 28,
    //        "prompt_tokens_details": {
    //            "cached_tokens": 0,
    //            "text_tokens": 0,
    //            "audio_tokens": 0,
    //            "image_tokens": 0
    //        },
    //        "completion_tokens_details": {
    //            "text_tokens": 0,
    //            "audio_tokens": 0
    //        }
    //    }

    @JSONField(name = "prompt_tokens")
    private int promptTokens;
    @JSONField(name = "completion_tokens")
    private int completionTokens;
    @JSONField(name = "total_tokens")
    private int totalTokens;
    @JSONField(name = "prompt_tokens_details")
    private PromptTokensDetails promptTokensDetails;
    @JSONField(name = "completion_tokens_details")
    private CompletionTokensDetails completionTokensDetails;

    @Data
    public static class PromptTokensDetails {
        @JSONField(name = "cached_tokens")
        private int cachedTokens;
        @JSONField(name = "text_tokens")
        private int textTokens;
        @JSONField(name = "audio_tokens")
        private int audioTokens;
        @JSONField(name = "image_tokens")
        private int imageTokens;
    }

    @Data
    public static class CompletionTokensDetails {
        @JSONField(name = "text_tokens")
        private int textTokens;
        @JSONField(name = "audio_tokens")
        private int audioTokens;
    }
}
