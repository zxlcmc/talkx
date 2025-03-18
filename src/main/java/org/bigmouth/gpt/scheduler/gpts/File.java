package org.bigmouth.gpt.scheduler.gpts;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;
import com.alibaba.fastjson.annotation.JSONField;

/**
 * @author huxiao
 * @date 2024/1/11
 * @since 1.0.0
 */

@Data
public class File {
    private String id;
    private String type;

    @JSONField(name = "file_id")
    @JsonAlias("file_id")
    private String fileId;

    private String location;
}

