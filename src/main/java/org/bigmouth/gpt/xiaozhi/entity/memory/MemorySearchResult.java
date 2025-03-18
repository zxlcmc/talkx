package org.bigmouth.gpt.xiaozhi.entity.memory;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class MemorySearchResult {

    private List<Memory> results;
}