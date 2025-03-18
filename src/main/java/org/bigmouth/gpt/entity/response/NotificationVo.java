package org.bigmouth.gpt.entity.response;

import lombok.Data;
import lombok.experimental.Accessors;
import org.bigmouth.gpt.entity.NotificationDto;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * @author huxiao
 * @date 2023/10/24
 * @since 1.0.0
 */
@Data
@Accessors(chain = true)
public class NotificationVo {
    /**
     * 消息ID
     */
    private Long id;

    /**
     * 消息标题
     */
    private String title;

    /**
     * 消息类型。1 普通消息
     */
    private Integer type;

    /**
     * 是否置顶。0 不置顶、1 置顶
     */
    private Integer pinned;

    /**
     * 消息文本内容
     */
    private String content;

    /**
     * 是否已读
     */
    private Integer read;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    public static NotificationVo of(NotificationDto dto) {
        if (Objects.isNull(dto)) {
            return null;
        }
        return new NotificationVo()
                .setId(dto.getId())
                .setTitle(dto.getTitle())
                .setType(dto.getType())
                .setPinned(dto.getPinned())
                .setContent(dto.getContent())
                .setRead(dto.getIsRead())
                .setCreateTime(dto.getCreateTime());
    }
}
