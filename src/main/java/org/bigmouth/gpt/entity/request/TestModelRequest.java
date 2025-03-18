package org.bigmouth.gpt.entity.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author Allen Hu
 * @date 2025/3/5
 */
@Data
public class TestModelRequest {
    /**
     * 选择的模型
     */
    @NotBlank(message = "模型不能为空")
    private String llmModel;

    private String proxyBaseUrl;

    private String apiKey;
}
