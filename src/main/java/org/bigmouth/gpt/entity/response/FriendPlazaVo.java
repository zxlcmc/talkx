package org.bigmouth.gpt.entity.response;

import lombok.Data;
import lombok.experimental.Accessors;
import org.bigmouth.gpt.entity.Friend;
import org.bigmouth.gpt.utils.Constants;

/**
 * 好友广场信息类
 * @author huxiao
 * @date 2023/12/13
 * @since 1.0.0
 */
@Data
@Accessors(chain = true)
public class FriendPlazaVo {
    /**
     * ID
     */
    private Long id;

    /**
     * 头像地址
     */
    private String avatar;

    /**
     * 好友名称
     */
    private String name;

    /**
     * 角色类型
     */
    private String roleType;

    /**
     * 招呼语
     */
    private String welcome;

    /**
     * 是否支持语音聊天。1 是，其他情况否。
     */
    private Integer voiceChat;

    /**
     * 简介
     */
    private String intro;

    /**
     * 标签，支持多个
     */
    private String tag;

    /**
     * 是否已经添加到好友列表中。1 是，其他情况否。
     */
    private int followed = Constants.NO;

    /**
     * AI 类型。1 普通、2 GPTs
     */
    private int friendType = Constants.Friend.FRIEND_TYPE_BASIC;

    public static FriendPlazaVo of(Friend friend) {
        return new FriendPlazaVo()
                .setId(friend.getId())
                .setAvatar(friend.getAvatar())
                .setName(friend.getName())
                .setRoleType(friend.getRoleType())
                .setWelcome(friend.getWelcome())
                .setVoiceChat(friend.getVoiceChat())
                .setIntro(friend.getIntro())
                .setTag(friend.getTag())
                .setFriendType(friend.getFriendType())
                ;
    }
}