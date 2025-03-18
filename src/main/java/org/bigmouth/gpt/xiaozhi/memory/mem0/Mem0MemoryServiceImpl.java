package org.bigmouth.gpt.xiaozhi.memory.mem0;

import org.bigmouth.gpt.xiaozhi.entity.memory.*;
import org.bigmouth.gpt.xiaozhi.forest.Mem0Api;
import org.bigmouth.gpt.xiaozhi.memory.MemoryService;
import org.bigmouth.gpt.xiaozhi.memory.MemoryType;

import java.util.List;
import java.util.Map;
import java.util.Optional;


public class Mem0MemoryServiceImpl implements MemoryService {

    private final Mem0Api mem0Api;

    public Mem0MemoryServiceImpl(Mem0Api mem0Api) {
        this.mem0Api = mem0Api;
    }

    @Override
    public MemoryType of() {
        return MemoryType.Mem0;
    }

    @Override
    public ActionResult setConfig(Map<String, Object> config) {
        return mem0Api.setConfig(config);
    }

    @Override
    public MemoryCreateResult addMemory(MemoryCreate memoryCreate) {
        return mem0Api.addMemory(memoryCreate);
    }

    @Override
    public MemorySearchResult getAllMemories(Long userId, String runId, Long agentId) {
        String userId1 = Optional.ofNullable(userId).map(Object::toString).orElse(null);
        String agentId1 = Optional.ofNullable(agentId).map(Object::toString).orElse(null);
        return mem0Api.getAllMemories(userId1, runId, agentId1);
    }

    @Override
    public Memory getMemory(String memoryId) {
        return mem0Api.getMemory(memoryId);
    }

    @Override
    public MemorySearchResult searchMemories(SearchRequest searchRequest) {
        return mem0Api.searchMemories(searchRequest);
    }

    @Override
    public List<Memory> memoryHistory(String memoryId) {
        return mem0Api.memoryHistory(memoryId);
    }

    @Override
    public ActionResult updateMemory(String memoryId, String updatedMemory) {
        return mem0Api.updateMemory(memoryId, updatedMemory);
    }

    @Override
    public ActionResult deleteMemory(String memoryId) {
        return mem0Api.deleteMemory(memoryId);
    }

    @Override
    public ActionResult deleteAllMemories(Long userId, String runId, Long agentId) {
        String userId1 = Optional.ofNullable(userId).map(Object::toString).orElse(null);
        String agentId1 = Optional.ofNullable(agentId).map(Object::toString).orElse(null);
        return mem0Api.deleteAllMemories(userId1, runId, agentId1);
    }

    @Override
    public ActionResult resetMemory() {
        return mem0Api.resetMemory();
    }
}