package org.bigmouth.gpt.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.bigmouth.gpt.ApplicationConfig;
import org.bigmouth.gpt.entity.*;
import org.bigmouth.gpt.interceptor.ContextFactory;
import org.bigmouth.gpt.service.IAttachmentService;
import org.bigmouth.gpt.service.ISessionMessageAttachRefService;
import org.bigmouth.gpt.service.ISessionMessageService;
import org.bigmouth.gpt.service.ISessionService;
import org.bigmouth.gpt.utils.Constants;
import org.hibernate.validator.constraints.Length;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author Allen Hu
 * @since 2023-06-12
 */
@Slf4j
@RestController
@RequestMapping("/session")
public class SessionController {

    public static final Long EMPTY_USERID = 0L;

    private final ApplicationConfig applicationConfig;
    private final ISessionService sessionService;
    private final ISessionMessageService sessionMessageService;
    private final ISessionMessageAttachRefService sessionMessageAttachRefService;
    private final IAttachmentService attachmentService;

    public SessionController(ApplicationConfig applicationConfig, ISessionService sessionService, ISessionMessageService sessionMessageService, ISessionMessageAttachRefService sessionMessageAttachRefService, IAttachmentService attachmentService) {
        this.applicationConfig = applicationConfig;
        this.sessionService = sessionService;
        this.sessionMessageService = sessionMessageService;
        this.sessionMessageAttachRefService = sessionMessageAttachRefService;
        this.attachmentService = attachmentService;
    }

    @GetMapping("/list")
    public ResponseEntity<PageVo<Session>> list(@Validated ListRequest listRequest) {
        Long userId = ContextFactory.getLoginUser().getId();
        IPage<Session> page = new Page<Session>()
                .setCurrent(listRequest.getCurrent())
                .setSize(listRequest.getSize());

        // 在插件里这个friendId 要求固定是 1，因为最早的版本开始，所有用户的编程助手话题记录都保持在 1 中。
        // 如果被调整了，那么话题记录将会不存在了。

        Session session = new Session()
                .setUserId(userId)
                .setProductId(listRequest.getProductId())
                .setFriendId(listRequest.getFriendId());

        IPage<Session> result = sessionService.page(page, Wrappers.query(session)
                .orderByDesc(Session.CREATE_TIME));
        return ResponseEntity.ok(new PageVo<>(result));
    }

    @GetMapping("/get")
    public ResponseEntity<PageVo<SessionMessage>> get(@Validated GetRequest getRequest) {
        String sessionId = getRequest.getSessionId();
        if (!checkExists(sessionId)) {
            return ResponseEntity.notFound().build();
        }
        IPage<SessionMessage> page = new Page<SessionMessage>()
                .setCurrent(getRequest.getCurrent())
                .setSize(getRequest.getSize());
        QueryWrapper<SessionMessage> query = Wrappers.query(new SessionMessage().setSessionId(sessionId));
        query.orderBy(true, Constants.YES == getRequest.getIsAsc(), SessionMessage.ID);
        IPage<SessionMessage> result = sessionMessageService.page(page, query);
        List<SessionMessage> records = result.getRecords();
        for (SessionMessage record : records) {
            boolean isIncludeAttachs = Objects.equals(Constants.YES, record.getIsIncludeAttachs());
            if (!isIncludeAttachs) {
                continue;
            }
            Long id = record.getId();
            List<SessionMessageAttachRef> refs = sessionMessageAttachRefService.list(Wrappers.query(new SessionMessageAttachRef().setMsgId(id)));
            List<AttachVo> attachments = Lists.newArrayList();
            for (SessionMessageAttachRef ref : refs) {
                Long attachId = ref.getAttachId();
                Attachment attachment = attachmentService.getByIdWithCache(attachId);
                if (Objects.isNull(attachment)) {
                    log.warn("Attachment {} does not exists!", attachId);
                } else {
                    attachments.add(AttachVo.of(attachment));
                }
            }
            record.setAttachments(attachments);
        }
        return ResponseEntity.ok(new PageVo<>(result));
    }

    @PostMapping("/new")
    public ResponseEntity<Object> newSession(@RequestBody @Validated NewRequest newRequest) {
        Long userId = getUserIdQuiet();
        Session session = new Session()
                .setId(sessionService.createSessionId())
                .setUserId(userId)
                .setProductId(newRequest.getProductId())
                .setFriendId(newRequest.getFriendId())
                .setTitle(newRequest.getTitle());
        if (!sessionService.save(session)) {
            throw new RuntimeException("unknown");
        }
        return ResponseEntity.ok(session);
    }

    @PostMapping("/save_msg")
    public ResponseEntity<SessionMessage> saveMsg(@RequestBody @Validated SaveMsg saveMsg) {
        return ResponseEntity.ok(sessionService.saveMessage(getUserIdQuiet(), saveMsg));
    }

    @PostMapping("/new_session_with_message")
    public ResponseEntity<Void> newSessionMessage(@RequestBody @Validated NewSessionWithMessage newSessionWithMessage) {
        Long userId = getUserIdQuiet();
        Session session = new Session()
                .setId(sessionService.createSessionId())
                .setUserId(userId)
                .setProductId(newSessionWithMessage.getProductId())
                .setFriendId(newSessionWithMessage.getFriendId())
                .setTitle(newSessionWithMessage.getTitle());
        if (!sessionService.save(session)) {
            throw new RuntimeException("unknown");
        }
        List<SaveMsg> saveMsgList = newSessionWithMessage.getSaveMsgList();

        saveMsgList.forEach(saveMsg -> {
            saveMsg.setSessionId(session.getId());
            sessionService.saveMessage(getUserIdQuiet(), saveMsg);
        });

        return ResponseEntity.ok().build();
    }

    @PostMapping("/delete_msg")
    public ResponseEntity<Object> deleteMsg(@RequestBody @Validated DeleteMsg deleteMsg) {
        Long messageId = deleteMsg.getMessageId();
        SessionMessage sessionMessage = sessionMessageService.getById(messageId);
        if (Objects.isNull(sessionMessage)) {
            return ResponseEntity.notFound().build();
        }
        boolean exists = checkExists(sessionMessage.getSessionId());
        if (!exists) {
            return ResponseEntity.notFound().build();
        }
        if (!sessionMessageService.removeById(messageId)) {
            throw new RuntimeException("unknown");
        }
        return ResponseEntity.ok().build();
    }

    @PostMapping("/delete")
    public ResponseEntity<Void> delete(@RequestBody @Validated DeleteRequest deleteRequest) {
        String sessionId = deleteRequest.getSessionId();
        if (!checkExists(sessionId)) {
            return ResponseEntity.notFound().build();
        }
        sessionService.deleteWithMessages(sessionId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/share")
    public ResponseEntity<Object> share(@RequestBody @Validated ShareSessionRequest request) {
        String sessionId = request.getSessionId();
        if (!checkExists(sessionId)) {
            return ResponseEntity.notFound().build();
        }
        sessionService.share(sessionId);

        StringBuilder fragment = new StringBuilder();
        fragment.append("/share");
        fragment.append("/").append(sessionId);

        User user = ContextFactory.getLoginUser();
        if (Objects.nonNull(user)) {
            fragment.append("/").append(user.getInviteCode());
        }

        String shareUrl = UriComponentsBuilder.fromUriString(applicationConfig.getTalkxHost())
                .fragment(fragment.toString())
                .build()
                .toString();
        return ResponseEntity.ok(new ShareSessionResponse().setShareUrl(shareUrl));
    }

    /**
     * <p>检查是否允许查看当前会话。</p>
     * <p>
     * 返回 true 的情况：
     * <li>会话属于游客</li>
     * <li>会话是公开分享的</li>
     * <li>会话属于当前登录的用户</li>
     * <p>
     * 其他情况则返回 false。
     *
     * @param sessionId 会话ID
     * @return 检查是否通过
     */
    private boolean checkExists(String sessionId) {
        Session session = sessionService.getById(sessionId);
        if (Objects.isNull(session)) {
            return false;
        }
        if (session.isSharedSession()) {
            return true;
        }
        Long userId = session.getUserId();
        Long loginUserId = getUserIdQuiet();
        return userId.equals(loginUserId) || userId.equals(EMPTY_USERID);
    }

    private Long getUserIdQuiet() {
        User user = ContextFactory.getLoginUser();
        return Optional.ofNullable(user)
                .map(User::getId)
                .orElse(EMPTY_USERID);
    }

    @Data
    public static class ListRequest extends PageRequest {
        private int productId;
        @NotNull(message = "好友id不能为空")
        private Long friendId;
    }

    @Data
    public static class GetRequest extends PageRequest {
        @NotBlank(message = "会话ID不能为空")
        private String sessionId;
        @NotNull(message = "排序策略不能为空")
        private Integer isAsc = Constants.NO;
    }

    @Data
    public static class NewRequest {
        private int productId;
        @NotBlank(message = "标题不能为空")
        @Length(max = 128, message = "标题内容长度不能超过128个字符")
        private String title;
        @NotNull(message = "好友不能为空")
        private Long friendId;
    }

    @Data
    public static class DeleteRequest {
        @NotBlank(message = "会话ID不能为空")
        private String sessionId;
    }

    @Data
    public static class SaveMsg {
        @NotBlank(message = "会话ID不能为空")
        private String sessionId;
        @NotBlank(message = "role不能为空")
        private String role;
        @NotNull(message = "消息内容不能为空")
        private String content;

        private Integer status = HttpStatus.OK.value();
        //        @DateTimeFormat(pattern = LocalDateTimeHelper.PATTERN_STR19)
        private LocalDateTime createTime;

        private List<AttachVo> attachments;
    }

    @Data
    public static class DeleteMsg {
        @NotNull(message = "消息ID不能为空")
        private Long messageId;
    }

    @Data
    public static class NewSessionWithMessage {
        private int productId;
        @NotBlank(message = "标题不能为空")
        @Length(max = 128, message = "标题内容长度不能超过128个字符")
        private String title;
        @NotNull(message = "好友不能为空")
        private Long friendId;

        @NotNull(message = "消息列表不能为空")
        List<SaveMsg> saveMsgList;
    }

    @Data
    public static class ShareSessionRequest {
        @NotBlank(message = "会话ID不能为空")
        private String sessionId;
    }

    @Data
    @Accessors(chain = true)
    public static class ShareSessionResponse {
        private String shareUrl;
    }
}
