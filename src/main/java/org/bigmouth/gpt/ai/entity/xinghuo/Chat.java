package org.bigmouth.gpt.ai.entity.xinghuo;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author huxiao
 * @date 2023/10/13
 * @since 1.0.0
 */
@Data
@Accessors(chain = true)
public class Chat {

    public static final String GENERAL = "general";
    public static final String GENERALV2 = "generalv2";
    public static final String GENERALV3 = "generalv3";

    /**
     * 指定访问的领域,general指向V1.5版本 generalv2指向V2版本。注意：不同的取值对应的url也不一样！
     */
    private String domain;
    /**
     * 核采样阈值。用于决定结果随机性，取值越高随机性越强即相同的问题得到的不同答案的可能性越高
     */
    private Float temperature = 0.5f;

    /**
     * 模型回答的tokens的最大长度。
     * V1.5取值为[1,4096]，V2.0取值为[1,8192]。默认为2048
     */
    @JSONField(name = "max_tokens")
    private Number maxTokens;

    /**
     * 从k个候选中随机选择⼀个（⾮等概率）
     * 取值为[1，6],默认为4
     */
    @JSONField(name = "top_k")
    private Number topK;

    /**
     * 需要保障用户下的唯一性。用于关联用户会话
     */
    @JSONField(name = "chat_id")
    private String chatId;
}
