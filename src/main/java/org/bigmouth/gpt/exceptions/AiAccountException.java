package org.bigmouth.gpt.exceptions;

import org.bigmouth.gpt.ai.entity.ApiKey;

/**
 * AI平台账户异常。该账户或Key不可用。
 *
 * @author huxiao
 * @date 2023/10/25
 * @since 1.0.0
 */
public class AiAccountException extends AiException {

    private int sc;
    private String code;
    private ApiKey apiKey;

    public AiAccountException(String message) {
        super(message);
    }

    public AiAccountException(ApiKey apiKey) {
        this.apiKey = apiKey;
    }

    public AiAccountException(String message, ApiKey apiKey) {
        super(message);
        this.apiKey = apiKey;
    }

    public AiAccountException(String message, Throwable cause) {
        super(message, cause);
    }

    public AiAccountException(Throwable cause) {
        super(cause);
    }

    public AiAccountException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public ApiKey getApiKey() {
        return apiKey;
    }

    public int getSc() {
        return sc;
    }

    public void setSc(int sc) {
        this.sc = sc;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setApiKey(ApiKey apiKey) {
        this.apiKey = apiKey;
    }
}
