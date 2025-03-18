package org.bigmouth.gpt.entity;

import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 好友权限表，私有AI需要该权限才能添加和访问
 * </p>
 *
 * @author allen
 * @since 2024-09-12
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class FriendPermission implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 好友ID
     */
    private Long friendId;


    public static final String USER_ID = "user_id";

    public static final String FRIEND_ID = "friend_id";

}
