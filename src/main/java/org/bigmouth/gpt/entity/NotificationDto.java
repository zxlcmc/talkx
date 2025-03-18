package org.bigmouth.gpt.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author huxiao
 * @date 2023/10/8
 * @since 1.0.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class NotificationDto extends Notification {

    /**
     * 是否已读
     */
    private Integer isRead;
}

