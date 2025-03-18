package org.bigmouth.gpt.entity;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author huxiao
 * @date 2024/4/16
 * @since 1.0.0
 */
@Data
public class TableSchemaVo {
    private Long id;

    private Long userId;

    /**
     * 表结构名称
     */
    private String name;

    /**
     * SQL语言类型
     */
    private String sqlType;

    /**
     * 表结构信息
     */
    private String schema;

    /**
     * 快速开始
     */
    private List<String> conversationStart;

    private LocalDateTime createTime;

    private LocalDateTime modifyTime;
}
