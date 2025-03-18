package org.bigmouth.gpt.xiaozhi.forest;

import com.dtflys.forest.annotation.*;
import org.bigmouth.gpt.xiaozhi.entity.memory.*;

import java.util.List;
import java.util.Map;

@BaseRequest(baseURL = "${mem0baseUrl}")
public interface Mem0Api {

    @Post("/configure")
    ActionResult setConfig(@Body Map<String, Object> config);

    @Post(value = "/memories", readTimeout = 10000)
    MemoryCreateResult addMemory(@JSONBody MemoryCreate memoryCreate);

    @Get("/memories")
    MemorySearchResult getAllMemories(
            @Query("user_id") String userId,
            @Query("run_id") String runId,
            @Query("agent_id") String agentId
    );

    @Get("/memories/{memoryId}")
    Memory getMemory(@Var("memoryId") String memoryId);

    @Post("/search")
    MemorySearchResult searchMemories(@JSONBody SearchRequest searchRequest);

    @Get("/memories/{memoryId}/history")
    List<Memory> memoryHistory(@Var("memoryId") String memoryId);

    @Put("/memories/{memoryId}")
    ActionResult updateMemory(@Var("memoryId") String memoryId, @Query("updated_memory") String updatedMemory);

    @Delete("/memories/{memoryId}")
    ActionResult deleteMemory(@Var("memoryId") String memoryId);

    @Delete("/memories")
    ActionResult deleteAllMemories(
            @Query("user_id") String userId,
            @Query("run_id") String runId,
            @Query("agent_id") String agentId
    );

    @Post("/reset")
    ActionResult resetMemory();
}
