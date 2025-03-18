package org.bigmouth.gpt.xiaozhi.entity.memory;

import lombok.Data;

import java.util.List;

/**
 * @author Allen Hu
 * @date 2025/3/11
 */
@Data
public class MemoryCreateResult {
    /**
     * {'results': [{'id': '2cf55875-1025-4543-acb8-f3292ea392ac', 'memory': '名字改成胡大嘴', 'event': 'UPDATE', 'previous_memory': '名字是胡笑'}]}
     */

    private List<MemoryCreateResultItem> results;

    @Data
    public static class MemoryCreateResultItem {
        private String id;
        private String memory;
        private String event;
        private String previous_memory;
    }
}
