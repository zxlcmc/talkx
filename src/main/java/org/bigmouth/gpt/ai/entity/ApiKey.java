package org.bigmouth.gpt.ai.entity;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author huxiao
 * @date 2023/9/21
 * @since 1.0.0
 */
@Data
@Accessors(chain = true)
public class ApiKey {

    /**
     * 是否用户私有的
     */
    private boolean userPrivate;

    /**
     * Api请求地址
     */
    private String apiUrl;
    /**
     * ApiKey
     */
    private String apiKey;

    public static ApiKey ofPublic(String apiUrl, String apiKey) {
        return new ApiKey().setUserPrivate(false).setApiUrl(apiUrl).setApiKey(apiKey);
    }

    public static ApiKey ofUserPrivate(String apiUrl, String apiKey) {
        return new ApiKey().setUserPrivate(true).setApiUrl(apiUrl).setApiKey(apiKey);
    }
}
