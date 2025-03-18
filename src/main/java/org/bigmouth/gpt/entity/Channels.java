package org.bigmouth.gpt.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.util.Objects;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author allen
 * @since 2023-12-28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class Channels implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long type;

    @TableField("`key`")
    private String key;

    private String openAiOrganization;

    private Long status;

    private String name;

    private Long weight;

    private Long createdTime;

    private Long testTime;

    private Long responseTime;

    private String baseUrl;

    private String other;

    private Double balance;

    private Long balanceUpdatedTime;

    private String models;

    @TableField("`group`")
    private String group;

    private Long usedQuota;

    private String modelMapping;

    private Long priority;

    private Long autoBan;


    public static final String ID = "id";

    public static final String TYPE = "type";

    public static final String KEY = "key";

    public static final String OPEN_AI_ORGANIZATION = "open_ai_organization";

    public static final String STATUS = "status";

    public static final String NAME = "name";

    public static final String WEIGHT = "weight";

    public static final String CREATED_TIME = "created_time";

    public static final String TEST_TIME = "test_time";

    public static final String RESPONSE_TIME = "response_time";

    public static final String BASE_URL = "base_url";

    public static final String OTHER = "other";

    public static final String BALANCE = "balance";

    public static final String BALANCE_UPDATED_TIME = "balance_updated_time";

    public static final String MODELS = "models";

    public static final String GROUP = "group";

    public static final String USED_QUOTA = "used_quota";

    public static final String MODEL_MAPPING = "model_mapping";

    public static final String PRIORITY = "priority";

    public static final String AUTO_BAN = "auto_ban";

    public boolean isDisabled() {
        return !Objects.equals(1L, status);
    }

    public boolean isEnabled() {
        return Objects.equals(1L, status);
    }
}
