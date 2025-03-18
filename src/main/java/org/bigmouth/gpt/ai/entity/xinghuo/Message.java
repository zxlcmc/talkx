package org.bigmouth.gpt.ai.entity.xinghuo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author huxiao
 * @date 2023/10/13
 * @since 1.0.0
 */
@Data
@Accessors(chain = true)
public class Message {
    private List<Text> text;
}
