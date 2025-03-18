package org.bigmouth.gpt.scheduler.gpts;

import lombok.Data;

import java.util.List;

/**
 * @author huxiao
 * @date 2024/1/11
 * @since 1.0.0
 */

@Data
public class Response {
    private List<Cut> cuts;
    private String locale;
}
