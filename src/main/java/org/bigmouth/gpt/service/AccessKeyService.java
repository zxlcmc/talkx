package org.bigmouth.gpt.service;

import org.bigmouth.gpt.ai.entity.ApiKey;
import org.bigmouth.gpt.entity.GetAccessKeyParam;

/**
 * @author allen
 * @date 2023/5/8
 * @since 1.0.0
 */
public interface AccessKeyService {

    /**
     * 获取可用的 API 密钥，但排除指定的 API密钥。
     * @param param 请求参数
     * @return 可用的 API 密钥对象
     */
    ApiKey getAvailable(GetAccessKeyParam param);
}
