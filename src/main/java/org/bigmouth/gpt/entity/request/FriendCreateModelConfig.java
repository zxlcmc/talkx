package org.bigmouth.gpt.entity.request;

import lombok.Data;

/**
 * @author huxiao
 * @date 2023/12/13
 * @since 1.0.0
 */
@Data
public class FriendCreateModelConfig {

    /**
     * 最大回复数
     */
    private int maxTokens = 1000;

    /**
     * 随机性
     */
    private double temperature = 1.0d;

    /**
     * 词汇属性
     */
    private double topP = 1.0d;

    /**
     * 话题新鲜度
     */
    private double presencePenalty = 0d;

    /**
     * 频率惩罚度
     */
    private double frequencyPenalty = 0d;
}
