package org.bigmouth.gpt.entity;

import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;
import java.util.Objects;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.bigmouth.gpt.utils.Constants;

/**
 * <p>
 * 
 * </p>
 *
 * @author allen
 * @since 2024-01-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("prompt_config")
public class PromptConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String roleType;

    /**
     * GPTs ID
     */
    private String gptsId;

    /**
     * 是否指定密钥。0 否、1 是
     */
    private Integer fixedKey;

    /**
     * 每次对话所需要的蒜粒数量
     */
    private BigDecimal coinCostPer;

    private String systemPrompt;

    private String contentPrompt;

    private String macro;

    private Integer messageContextSize;

    private String openaiRequestBody;

    private String xinghuoRequestBody;

    private LocalDateTime createTime;

    private LocalDateTime modifyTime;

    private Integer deleted;


    public static final String ID = "id";

    public static final String ROLE_TYPE = "role_type";

    public static final String GPTS_ID = "gpts_id";

    public static final String FIXED_KEY = "fixed_key";

    public static final String COIN_COST_PER = "coin_cost_per";

    public static final String SYSTEM_PROMPT = "system_prompt";

    public static final String CONTENT_PROMPT = "content_prompt";

    public static final String MACRO = "macro";

    public static final String MESSAGE_CONTEXT_SIZE = "message_context_size";

    public static final String OPENAI_REQUEST_BODY = "openai_request_body";

    public static final String XINGHUO_REQUEST_BODY = "xinghuo_request_body";

    public static final String CREATE_TIME = "create_time";

    public static final String MODIFY_TIME = "modify_time";

    public static final String DELETED = "deleted";

    public boolean isFixedKey0() {
        return Objects.equals(Constants.YES, getFixedKey());
    }
}
