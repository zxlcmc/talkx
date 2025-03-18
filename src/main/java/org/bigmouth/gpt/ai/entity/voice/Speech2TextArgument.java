package org.bigmouth.gpt.ai.entity.voice;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.bigmouth.gpt.ai.entity.ApiKey;
import org.bigmouth.gpt.entity.User;

import java.util.Set;

/**
 * @author allen
 * @date 2023/5/6
 * @since 1.0.0
 */
@Data
@Builder
@AllArgsConstructor
public class Speech2TextArgument {

    private String id;
    /**
     * 用户信息，游客时是null
     */
    private User user;
    /**
     * ApiKey
     */
    private ApiKey apiKey;

    private String originalFilename;

    private String contentType;

    private byte[] bytes;
    /**
     * 明确本次访问需要排除的 ApiKey
     */
    private Set<String> excludeApiKey;
}
