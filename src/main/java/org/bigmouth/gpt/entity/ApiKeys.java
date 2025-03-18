package org.bigmouth.gpt.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 访问密钥
 * </p>
 *
 * @author allen
 * @since 2023-09-21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("api_keys")
public class ApiKeys implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 密钥ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 平台类型。1 OpenAI
     */
    private Integer platformType;

    /**
     * ApiKey
     */
    private String apiKey;
    /**
     * 模型请求地址，如有值优先使用这个地址请求。
     */
    private String requestUrl;

    /**
     * Organization ID
     */
    private String organizationId;

    /**
     * 适用的模型。比如平台类型是OpenAI时，1 表示所有的gpt-3.5，2 表示所有的gpt-4
     */
    private Integer adaptedModel;

    /**
     * 适用的角色，如果设置了角色只能给对应角色使用。
     */
    private String adaptedRoleType;

    /**
     * 每日限流次数。-1 表示不限
     */
    private Integer limitForDate;

    /**
     * 每小时限流次数。-1 表示不限
     */
    private Integer limitForHour;

    /**
     * 每分钟限流次数。-1 表示不限
     */
    private Integer limitForMinute;

    /**
     * 状态。0 不可用，1 正常，-1 今日限流，-2 分钟限流，-3 小时限流
     */
    private Integer status;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    private LocalDateTime modifyTime;


    public static final String ID = "id";

    public static final String PLATFORM_TYPE = "platform_type";

    public static final String API_KEY = "api_key";

    public static final String REQUEST_URL = "request_url";

    public static final String ORGANIZATION_ID = "organization_id";

    public static final String ADAPTED_MODEL = "adapted_model";

    public static final String ADAPTED_ROLE_TYPE = "adapted_role_type";

    public static final String LIMIT_FOR_DATE = "limit_for_date";

    public static final String LIMIT_FOR_HOUR = "limit_for_hour";

    public static final String LIMIT_FOR_MINUTE = "limit_for_minute";

    public static final String STATUS = "status";

    public static final String CREATE_TIME = "create_time";

    public static final String MODIFY_TIME = "modify_time";

}
