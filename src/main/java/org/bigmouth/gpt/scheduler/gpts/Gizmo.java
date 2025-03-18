package org.bigmouth.gpt.scheduler.gpts;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.List;

/**
 * @author huxiao
 * @date 2024/1/11
 * @since 1.0.0
 */
@Data
public class Gizmo {
    private String id;
    private String organizationId;
    private String shortUrl;

    @JSONField(name = "author")
    private Author author;

    private String workspaceId;
    private String model;
    private String instructions;
    private String settings;

    private Display display;

    private String shareRecipient;
    private String updatedAt;
    private String lastInteractedAt;
    private List<String> tags;
    private String version;
    private String liveVersion;
    private String trainingDisabled;
    private String allowedSharingRecipients;
    private String reviewInfo;
    private String appealInfo;
}

