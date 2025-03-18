package org.bigmouth.gpt.ai.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author allen
 * @date 2023/4/28
 * @since 1.0.0
 */
@Getter
@AllArgsConstructor
public enum GptRole {

    /**
     * 系统
     */
    SYSTEM("system"),
    /**
     * 助手
     */
    ASSISTANT("assistant"),
    /**
     * 用户
     */
    USER("user"),

    DEVELOPER("developer");

    private final String name;
}
