package org.bigmouth.gpt.ai.entity.voice;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author huxiao
 * @date 2023/11/10
 * @since 1.0.0
 */
@Data
public class Tts1Request {

    private String model = "tts-1";
    @NotBlank private String input;
    private String voice = "onyx";
    @JSONField(name = "response_format")
    private String responseFormat = "mp3";
    private Number speed = 1.0;
}
