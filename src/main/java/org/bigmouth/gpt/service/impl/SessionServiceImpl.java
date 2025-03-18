package org.bigmouth.gpt.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bxm.warcar.cache.Fetcher;
import com.bxm.warcar.cache.Updater;
import com.bxm.warcar.id.IdGenerator;
import com.bxm.warcar.utils.DateHelper;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.bigmouth.gpt.ai.entity.GptRole;
import org.bigmouth.gpt.controller.SessionController;
import org.bigmouth.gpt.entity.*;
import org.bigmouth.gpt.mapper.talkx.SessionMapper;
import org.bigmouth.gpt.service.IAttachmentService;
import org.bigmouth.gpt.service.ISessionMessageAttachRefService;
import org.bigmouth.gpt.service.ISessionMessageService;
import org.bigmouth.gpt.service.ISessionService;
import org.bigmouth.gpt.utils.Constants;
import org.bigmouth.gpt.utils.RedisKeys;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Allen Hu
 * @since 2023-06-12
 */
@Service
public class SessionServiceImpl extends ServiceImpl<SessionMapper, Session> implements ISessionService {

    private final ISessionMessageService sessionMessageService;
    private final IAttachmentService attachmentService;
    private final ISessionMessageAttachRefService sessionMessageAttachRefService;
    private final IdGenerator idGenerator;
    private final Fetcher fetcher;
    private final Updater updater;

    public SessionServiceImpl(ISessionMessageService sessionMessageService, IAttachmentService attachmentService, ISessionMessageAttachRefService sessionMessageAttachRefService, IdGenerator idGenerator, Fetcher fetcher, Updater updater) {
        this.sessionMessageService = sessionMessageService;
        this.attachmentService = attachmentService;
        this.sessionMessageAttachRefService = sessionMessageAttachRefService;
        this.idGenerator = idGenerator;
        this.fetcher = fetcher;
        this.updater = updater;
    }

    @Override
    public String createSessionId() {
        return idGenerator.next();
    }

    @Override
    @Transactional(rollbackFor = Exception.class, timeout = 30)
    public void deleteWithMessages(String sessionId) {
        List<SessionMessage> sessionMessages = sessionMessageService.list(Wrappers.query(new SessionMessage().setSessionId(sessionId)));
        if (CollectionUtils.isNotEmpty(sessionMessages)) {
            sessionMessages.forEach(sessionMessage -> {
                Long messageId = sessionMessage.getId();
                sessionMessageAttachRefService.remove(Wrappers.query(new SessionMessageAttachRef().setMsgId(messageId)));
            });

            sessionMessageService.remove(Wrappers.query(new SessionMessage().setSessionId(sessionId)));
        }
        this.removeById(sessionId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SessionMessage saveMessage(long userId, SessionController.SaveMsg saveMsg) {
        String sessionId = saveMsg.getSessionId();
        Session existsSession = super.getOne(Wrappers.query(new Session().setId(sessionId)));
        if (Objects.isNull(existsSession)) {
            throw new IllegalArgumentException("Not exists session: " + sessionId);
        }
        long existsUserId = Optional.ofNullable(existsSession.getUserId()).orElse(SessionController.EMPTY_USERID);
        boolean allow = existsUserId == SessionController.EMPTY_USERID || userId == existsUserId;
        if (!allow) {
            throw new IllegalStateException("Not allow operation");
        }
        String role = saveMsg.getRole();
        if (!StringUtils.equals(role, GptRole.USER.getName())
                && !StringUtils.equals(role, GptRole.ASSISTANT.getName())) {
            throw new IllegalArgumentException("Unknown role: " + role);
        }
        List<AttachVo> attachments = saveMsg.getAttachments();
        boolean notEmptyAttachments = CollectionUtils.isNotEmpty(attachments);
        SessionMessage sessionMessage = new SessionMessage()
                .setSessionId(sessionId)
                .setRole(role)
                .setContent(saveMsg.getContent())
                .setStatus(saveMsg.getStatus())
                .setIsIncludeAttachs(notEmptyAttachments ? Constants.YES : Constants.NO)
                .setCreateTime(saveMsg.getCreateTime());
        boolean save = sessionMessageService.save(sessionMessage);
        if (!save) {
            throw new RuntimeException("Save fail");
        }
        if (notEmptyAttachments) {
            for (AttachVo attachment : attachments) {
                Attachment attach = new Attachment()
                        .setName(attachment.getName())
                        .setMimeType(attachment.getMimeType())
                        .setSize(attachment.getSize())
                        .setUrl(attachment.getUrl());
                attachmentService.save(attach);

                SessionMessageAttachRef ref = new SessionMessageAttachRef()
                        .setMsgId(sessionMessage.getId())
                        .setAttachId(attach.getId());
                sessionMessageAttachRefService.save(ref);
            }
            sessionMessage.setIsIncludeAttachs(Constants.YES);
            sessionMessage.setAttachments(attachments);
        }
        return sessionMessage;
    }

    @Override
    public Session share(String sessionId) {
        Session session = super.getById(sessionId);
        session.setShared(Constants.YES);
        session.setModifyTime(LocalDateTime.now());
        super.updateById(session);
        return session;
    }


    @Override
    public Session createKeepSessionIfNecessary(String udpSessionId, Long userId, Long friendId, Integer productId, Duration sessionKeepTime) {
        int expireTimeInSec = (int) sessionKeepTime.getSeconds();
        Session cached = fetcher.fetch(RedisKeys.AboutSession.stringLastSession(userId, friendId, productId), () -> {
            Session session = new Session()
                    .setId(createSessionId())
                    .setUdpSessionId(udpSessionId)
                    .setUserId(userId)
                    .setFriendId(friendId)
                    .setProductId(productId)
                    .setTitle(DateHelper.format("yyyy-MM-dd HH:mm"))
                    .setCreateTime(LocalDateTime.now());
//            save(session);
            // 先不保存，因为有可能开始对话了，但是没有发生聊天记录，所以改在对话时保存
            return session;
        }, Session.class, expireTimeInSec);
        updater.expire(RedisKeys.AboutSession.stringLastSession(userId, friendId, productId), expireTimeInSec);
        return cached;
    }

    @Override
    public void removeKeepSession(Long userId, Long friendId, Integer productId) {
        updater.remove(RedisKeys.AboutSession.stringLastSession(userId, friendId, productId));
    }
}
