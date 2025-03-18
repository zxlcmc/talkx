package org.bigmouth.gpt.scheduler.gpts;

import lombok.Data;

import java.util.List;
/**
 * @author huxiao
 * @date 2024/1/11
 * @since 1.0.0
 */

@Data
public class Resource {
    private Gizmo gizmo;

//    private List<Tool> tools;

    private List<File> files;
}

