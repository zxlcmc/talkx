package org.bigmouth.gpt.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author allen
 * @since 2025-02-27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("`device`")
@Accessors(chain = true)
public class Device implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 用户的好友ID
     */
    private Long userFriendId;

    /**
     * 好友ID
     */
    private Long friendId;

    /**
     * 角色类型
     */
    private String roleType;

    /**
     * 设备型号
     */
    private String deviceModel;

    /**
     * 芯片型号
     */
    private String chipModelName;

    /**
     * 固件版本
     */
    private String firmwareVersion;

    /**
     * mac地址
     */
    private String macAddress;

    /**
     * 绑定时间
     */
    private LocalDateTime bindTime;

    /**
     * 是否支持OTA更新，0 不支持，1 支持
     */
    private Integer otaUpdate;

    private String remark;

    private LocalDateTime createTime;

    private LocalDateTime modifyTime;

    /**
     * 删除标识
     */
    private Integer deleted;


    public static final String ID = "id";

    public static final String USER_ID = "user_id";

    public static final String USER_FRIEND_ID = "user_friend_id";

    public static final String FRIEND_ID = "friend_id";

    public static final String ROLE_TYPE = "role_type";

    public static final String DEVICE_MODEL = "device_model";

    public static final String CHIP_MODEL_NAME = "chip_model_name";

    public static final String FIRMWARE_VERSION = "firmware_version";

    public static final String MAC_ADDRESS = "mac_address";

    public static final String BIND_TIME = "bind_time";

    public static final String OTA_UPDATE = "ota_update";

    public static final String REMARK = "remark";

    public static final String CREATE_TIME = "create_time";

    public static final String MODIFY_TIME = "modify_time";

    public static final String DELETED = "deleted";

}
