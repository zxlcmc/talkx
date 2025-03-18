package org.bigmouth.gpt.controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.bigmouth.gpt.entity.User;
import org.bigmouth.gpt.interceptor.ContextFactory;
import org.bigmouth.gpt.xiaozhi.memory.MemoryService;
import org.bigmouth.gpt.xiaozhi.memory.MemoryServiceFactory;
import org.bigmouth.gpt.xiaozhi.entity.memory.ActionResult;
import org.bigmouth.gpt.xiaozhi.entity.memory.Memory;
import org.bigmouth.gpt.xiaozhi.entity.memory.MemorySearchResult;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import java.util.Objects;

/**
 * @author Allen Hu
 * @date 2025/3/11
 */
@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/friend/memory")
public class FriendMemoryController {

    private final MemoryServiceFactory memoryServiceFactory;

    @GetMapping("/list")
    public ResponseEntity<Object> list(@RequestParam Long userFriendId) {
        User user = ContextFactory.getLoginUser();
        MemoryService memoryService = memoryServiceFactory.getDefault();
        if (Objects.isNull(memoryService)) {
            return ResponseEntity.ok().build();
        }
        MemorySearchResult memories = memoryService.getAllMemories(user.getId(), null, userFriendId);
        return ResponseEntity.ok(memories);
    }

    @PostMapping("/update")
    public ResponseEntity<Object> update(@RequestBody @Validated UpdateMemoryRequest updateMemoryRequest) {
        String memoryId = updateMemoryRequest.getMemoryId();
        String updatedMemory = updateMemoryRequest.getUpdatedMemory();
        User user = ContextFactory.getLoginUser();
        MemoryService memoryService = memoryServiceFactory.getDefault();
        if (Objects.isNull(memoryService)) {
            return ResponseEntity.ok().build();
        }
        Memory memory = memoryService.getMemory(memoryId);
        if (memory == null || !StringUtils.equals(memory.getUserId(), user.getId().toString())) {
            return ResponseEntity.notFound().build();
        }
        ActionResult result = memoryService.updateMemory(memoryId, updatedMemory);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/delete")
    public ResponseEntity<Object> delete(@RequestBody @Validated DeleteMemoryRequest deleteMemoryRequest) {
        String memoryId = deleteMemoryRequest.getMemoryId();
        User user = ContextFactory.getLoginUser();
        MemoryService memoryService = memoryServiceFactory.getDefault();
        if (Objects.isNull(memoryService)) {
            return ResponseEntity.ok().build();
        }
        Memory memory = memoryService.getMemory(memoryId);
        if (memory == null || !StringUtils.equals(memory.getUserId(), user.getId().toString())) {
            return ResponseEntity.notFound().build();
        }
        ActionResult result = memoryService.deleteMemory(memoryId);
        return ResponseEntity.ok(result);
    }

    @Data
    public static class UpdateMemoryRequest {
        @NotBlank(message = "记忆ID不能为空")
        private String memoryId;
        @NotBlank(message = "更新后的记忆不能为空")
        private String updatedMemory;
    }

    @Data
    public static class DeleteMemoryRequest {
        @NotBlank(message = "记忆ID不能为空")
        private String memoryId;
    }
}
