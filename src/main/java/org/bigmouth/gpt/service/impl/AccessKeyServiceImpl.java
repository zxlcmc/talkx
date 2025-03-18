package org.bigmouth.gpt.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.bigmouth.gpt.ApplicationConfig;
import org.bigmouth.gpt.ai.entity.ApiKey;
import org.bigmouth.gpt.entity.Friend;
import org.bigmouth.gpt.entity.GetAccessKeyParam;
import org.bigmouth.gpt.entity.User;
import org.bigmouth.gpt.service.AccessKeyService;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @author huxiao
 * @date 2023/9/1
 * @since 1.0.0
 */
@Slf4j
@Primary
@Service
public class AccessKeyServiceImpl implements AccessKeyService {

    private final ApplicationConfig applicationConfig;

    public AccessKeyServiceImpl(ApplicationConfig applicationConfig) {
        this.applicationConfig = applicationConfig;
    }

    @Override
    public ApiKey getAvailable(GetAccessKeyParam param) {
        User user = param.getUser();
        Friend friend = param.getFriend();

        // 优先使用好友的配置
        if (Objects.nonNull(friend)) {
            String requestUrl = friend.getRequestUrl();
            String apiKey = friend.getApiKey();
            if (StringUtils.isNotBlank(requestUrl)) {
                return ApiKey.ofUserPrivate(requestUrl, apiKey);
            }
        }

        // 获取用户专属的Key
        if (Objects.nonNull(user)) {
            String proxyBaseUrl = user.getProxyBaseUrl();
            String apiKey = user.getApiKey();
            if (StringUtils.isNotBlank(proxyBaseUrl) || StringUtils.isNotBlank(apiKey)) {
                return ApiKey.ofUserPrivate(proxyBaseUrl, apiKey);
            }
        }

        // 获取当前可用的Key
        return ApiKey.ofPublic(applicationConfig.getLlmApiHost(), applicationConfig.getLlmApiKey());
    }
}
