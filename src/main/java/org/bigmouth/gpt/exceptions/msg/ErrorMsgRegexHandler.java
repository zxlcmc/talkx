package org.bigmouth.gpt.exceptions.msg;

import org.bigmouth.gpt.exceptions.AiAccountException;

/**
 * @author allen
 * @date 2023/7/13
 * @since 1.0.0
 */
public interface ErrorMsgRegexHandler {

    /**
     * 指定匹配的正则表达式
     * @return 正则表达式，默认返回null
     */
    default String getRegex() {
        return null;
    }

    /**
     * 指定匹配的状态码
     * @return 默认-1，表示不匹配
     */
    default int getStatusCode() {
        return -1;
    }

    /**
     * 获取返回给用户的异常提示消息
     * @param exception AiAccountException
     * @return 异常提示消息
     */
    default String getMessage(AiAccountException exception) {
        return null;
    }

    /**
     * 执行其他处理
     * @param exception  AiAccountException
     */
    default void afterHandle(AiAccountException exception) {
    }
}
