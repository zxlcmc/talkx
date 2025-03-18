package org.bigmouth.gpt.xiaozhi.entity.asr;

import com.google.common.collect.Lists;
import lombok.Data;

import java.util.List;

/**
 * @author Allen Hu
 * @date 2025/3/17
 */
@Data
public class SenseVoiceFastApiResult {

    /*
    {
    "result": [
            {
                "key": "test.wav",
                "text": "闭上眼睛听听内心的声音感受这份宁静美好",
                "raw_text": "<|zh|><|NEUTRAL|><|Speech|><|woitn|>闭上眼睛听听内心的声音感受这份宁静美好",
                "clean_text": "闭上眼睛听听内心的声音感受这份宁静美好"
            }
        ]
    }
     */

    private List<Item> result = Lists.newArrayList();

    @Data
    public static class Item {
        private String key;
        private String text;
        private String raw_text;
        private String clean_text;
    }
}
