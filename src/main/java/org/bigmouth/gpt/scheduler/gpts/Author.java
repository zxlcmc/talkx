package org.bigmouth.gpt.scheduler.gpts;

import lombok.Data;

/**
 * @author huxiao
 * @date 2024/1/11
 * @since 1.0.0
 */
@Data
public class Author {
    private String userId;
    private String displayName;
    private String linkTo;
    private String selectedDisplay;
    private boolean isVerified;
    private boolean willReceiveSupportEmails;
}

