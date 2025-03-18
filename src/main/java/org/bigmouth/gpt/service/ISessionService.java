package org.bigmouth.gpt.service;

import org.bigmouth.gpt.controller.SessionController;
import org.bigmouth.gpt.entity.Session;
import com.baomidou.mybatisplus.extension.service.IService;
import org.bigmouth.gpt.entity.SessionMessage;

import java.time.Duration;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author Allen Hu
 * @since 2023-06-12
 */
public interface ISessionService extends IService<Session> {

    /**
     * Generates a new session ID.
     *
     * @return The newly generated session ID.
     */
    String createSessionId();

    /**
     * This method is used to delete a session along with its associated messages.
     *
     * @param sessionId The ID of the session to be deleted.
     */
    void deleteWithMessages(String sessionId);

    SessionMessage saveMessage(long userId, SessionController.SaveMsg saveMsg);

    /**
     * 公开指定会话。
     *
     * @param sessionId 会话ID
     */
    Session share(String sessionId);

    /**
     * 创建会话，如果不存在，则创建。
     * @param udpSessionId UDP会话ID
     * @param userId 用户ID
     * @param friendId 好友ID
     * @param productId 产品ID
     * @param sessionKeepTime 会话保持时长
     * @return 会话
     */
    Session createKeepSessionIfNecessary(String udpSessionId,
                                         Long userId, Long friendId, Integer productId,
                                         Duration sessionKeepTime);

    /**
     * 移除会话
     * @param userId 用户ID
     * @param friendId 好友ID
     * @param productId 产品ID
     */
    void removeKeepSession(Long userId, Long friendId, Integer productId);
}
