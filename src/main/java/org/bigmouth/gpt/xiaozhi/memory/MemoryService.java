package org.bigmouth.gpt.xiaozhi.memory;

import org.bigmouth.gpt.xiaozhi.entity.memory.*;

import java.util.List;
import java.util.Map;

public interface MemoryService {

    MemoryType of();

    ActionResult setConfig(Map<String, Object> config);

    MemoryCreateResult addMemory(MemoryCreate memoryCreate);

    MemorySearchResult getAllMemories(
            Long userId,
            String runId,
            Long agentId
    );

    
    Memory getMemory(String memoryId);

    
    MemorySearchResult searchMemories(SearchRequest searchRequest);

    
    List<Memory> memoryHistory(String memoryId);

    ActionResult updateMemory(String memoryId, String updatedMemory);

    ActionResult deleteMemory(String memoryId);

    
    ActionResult deleteAllMemories(
            Long userId,
            String runId,
            Long agentId
    );

    
    ActionResult resetMemory();
}
