package org.bigmouth.gpt.ai;

/**
 * @author huxiao
 * @date 2023/11/8
 * @since 1.0.0
 */
public interface OpenAiModelAdapter {

    /**
     * 返回角色类型
     *
     * @return 返回null表示GPT-Chat模型
     */
    String getRoleType();

    /**
     * 模型名称
     *
     * @return 返回null表示GPT-Chat模型
     */
    default String getModelName() {
        return null;
    }
}
