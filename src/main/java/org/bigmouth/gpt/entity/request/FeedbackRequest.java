package org.bigmouth.gpt.entity.request;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author tangxiao
 * @date 2023/7/26
 * @since 1.0
 */
@Data
@Accessors(chain = true)
public class FeedbackRequest {
    /**
     * 反馈内容
     */
    @NotBlank
    private String content;

    /**
     * 图片，多个逗号分隔
     */
    private String image;

    /**
     * 联系电话
     */
    private String contactPhone;
}
