package org.bigmouth.gpt.xiaozhi.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Allen Hu
 * @date 2025/2/20
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AudioParams {

    // {
    //        "format": "opus",
    //        "sample_rate": 16000,
    //        "channels": 1,
    //        "frame_duration": 60
    //    }

    private String format;
    @JSONField(name = "sample_rate")
    private Integer sampleRate;
    private Integer channels;
    @JSONField(name = "frame_duration")
    private Integer frameDuration;
}
