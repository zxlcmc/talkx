package org.bigmouth.gpt.scheduler.gpts;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;

import java.util.List;

/**
 * @author huxiao
 * @date 2024/1/11
 * @since 1.0.0
 */
@Data
public class Display {
    private String name;
    private String description;
    private String welcomeMessage;

    @JsonAlias("prompt_starters")
    @JSONField(name = "prompt_starters")
    private List<String> promptStarters;

    @JsonAlias("profile_picture_url")
    @JSONField(name = "profile_picture_url")
    private String profilePictureUrl;

    private List<String> categories;
}
