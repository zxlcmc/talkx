package org.bigmouth.gpt.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.bigmouth.gpt.utils.Constants;

import java.io.Serializable;
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
public class Session implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    /**
     * 0 Web (default),1 DingTalk,2 MicroProgram,3 IntelliJ_Idea,4 Vs_Code,
     */
    private Integer productId;

    private Long userId;

    private String title;

    /**
     * 好友id
     */
    private Long friendId;

    /**
     * 是否分享。0 不分享，1 分享
     */
    private Integer shared;

    private String udpSessionId;

    private LocalDateTime createTime;

    private LocalDateTime modifyTime;


    public static final String ID = "id";

    public static final String PRODUCT_ID = "product_id";

    public static final String USER_ID = "user_id";

    public static final String TITLE = "title";

    public static final String SHARED = "shared";

    public static final String UDP_SESSION_ID = "udp_session_id";

    public static final String CREATE_TIME = "create_time";

    public static final String MODIFY_TIME = "modify_time";

    /**
     * 返回会话是否公开分享
     * @return true 表示公开分享
     */
    public boolean isSharedSession() {
        return Objects.equals(Constants.YES, shared);
    }
}
