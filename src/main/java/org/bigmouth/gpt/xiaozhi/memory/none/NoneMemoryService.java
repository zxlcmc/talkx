package org.bigmouth.gpt.xiaozhi.memory.none;

import com.google.common.collect.Lists;
import org.bigmouth.gpt.xiaozhi.entity.memory.*;
import org.bigmouth.gpt.xiaozhi.memory.MemoryService;
import org.bigmouth.gpt.xiaozhi.memory.MemoryType;

import java.util.List;
import java.util.Map;

/**
 * @author Allen Hu
 * @date 2025/3/18
 */
public class NoneMemoryService implements MemoryService {

    @Override
    public MemoryType of() {
        return MemoryType.None;
    }

    @Override
    public ActionResult setConfig(Map<String, Object> config) {
        return new ActionResult();
    }

    @Override
    public MemoryCreateResult addMemory(MemoryCreate memoryCreate) {
        return new MemoryCreateResult();
    }

    @Override
    public MemorySearchResult getAllMemories(Long userId, String runId, Long agentId) {
        return new MemorySearchResult();
    }

    @Override
    public Memory getMemory(String memoryId) {
        return null;
    }

    @Override
    public MemorySearchResult searchMemories(SearchRequest searchRequest) {
        return new MemorySearchResult();
    }

    @Override
    public List<Memory> memoryHistory(String memoryId) {
        return Lists.newArrayList();
    }

    @Override
    public ActionResult updateMemory(String memoryId, String updatedMemory) {
        return new ActionResult();
    }

    @Override
    public ActionResult deleteMemory(String memoryId) {
        return new ActionResult();
    }

    @Override
    public ActionResult deleteAllMemories(Long userId, String runId, Long agentId) {
        return new ActionResult();
    }

    @Override
    public ActionResult resetMemory() {
        return new ActionResult();
    }
}
