package org.bigmouth.gpt.service;

import org.bigmouth.gpt.entity.Prompt;
import org.springframework.lang.Nullable;

/**
 * @author allen
 * @date 2023/5/15
 * @since 1.0.0
 */
public interface PromptService {

    String createRoleType();

    Prompt getWithMacros(@Nullable Long userId, Integer productType, String roleType);
}
