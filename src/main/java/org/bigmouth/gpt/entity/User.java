package org.bigmouth.gpt.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * <p>
 * 
 * </p>
 *
 * @author Allen Hu
 * @since 2023-06-12
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String email;

    private String phoneNum;

    private String name;

    private String password;

    /**
     * 0、禁用；1、可用
     */
    private Integer status;

    /**
     * 选择的模型
     */
    private String model;

    /**
     * 代理地址
     */
    private String proxyBaseUrl;

    /**
     * 用户的api key (代理)
     */
    private String apiKey;

    /**
     * 用户头像
     */
    private String avatar;

    /**
     * 邀请码
     */
    private String inviteCode;

    /**
     * 当前蒜粒
     */
    private BigDecimal coin;

    /**
     * 用于WebSocket通信的ApiKey
     */
    private String websocketApiKey;

    private LocalDateTime createTime;

    private LocalDateTime modifyTime;

    private Integer deleted;


    public static final String ID = "id";

    public static final String EMAIL = "email";

    public static final String PHONE_NUM = "phone_num";

    public static final String NAME = "name";

    public static final String PASSWORD = "password";

    public static final String STATUS = "status";

    public static final String MODEL = "model";

    public static final String PROXY_BASE_URL = "proxy_base_url";

    public static final String API_KEY = "api_key";

    public static final String AVATAR = "avatar";

    public static final String INVITE_CODE = "invite_code";

    public static final String COIN = "coin";

    public static final String WEBSOCKET_API_KEY = "websocket_api_key";

    public static final String CREATE_TIME = "create_time";

    public static final String MODIFY_TIME = "modify_time";

    public static final String DELETED = "deleted";

    public boolean isAvailableStatus() {
        return Objects.equals(1, status);
    }

    public String getSelectedModelName() {
        return getModel();
    }
}
