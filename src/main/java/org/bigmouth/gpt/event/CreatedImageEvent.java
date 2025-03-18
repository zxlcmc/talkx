package org.bigmouth.gpt.event;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.bigmouth.gpt.ai.entity.DalleApiRequest;

import java.util.EventObject;

/**
 * @author huxiao
 * @date 2023/12/6
 * @since 1.0.0
 */
@Setter
@Getter
@Accessors(chain = true)
public class CreatedImageEvent extends EventObject {

    private Long userId;
    private String url;
    private String revisedPrompt;
    private DalleApiRequest dalleApiRequest;

    public CreatedImageEvent(Object source) {
        super(source);
    }
}
