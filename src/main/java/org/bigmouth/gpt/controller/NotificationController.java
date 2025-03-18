package org.bigmouth.gpt.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;
import org.bigmouth.gpt.entity.*;
import org.bigmouth.gpt.entity.request.KeepAliveRequest;
import org.bigmouth.gpt.entity.response.NotificationVo;
import org.bigmouth.gpt.exceptions.ForbiddenException;
import org.bigmouth.gpt.interceptor.ContextFactory;
import org.bigmouth.gpt.service.INotificationReadMarkService;
import org.bigmouth.gpt.service.INotificationService;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * <p>
 * 消息信息 前端控制器
 * </p>
 *
 * @author allen
 * @since 2023-10-08
 */
@RestController
@RequestMapping("/notification")
public class NotificationController {

    private final INotificationService notificationService;
    private final INotificationReadMarkService notificationReadMarkService;

    public NotificationController(INotificationService notificationService, INotificationReadMarkService notificationReadMarkService) {
        this.notificationService = notificationService;
        this.notificationReadMarkService = notificationReadMarkService;
    }

    /**
     * @return
     * @see IndexController#keepAlive(KeepAliveRequest)
     */
    @Deprecated
    @GetMapping("/select_pinned_one")
    public ResponseEntity<NotificationVo> selectPinnedOne() {
        User user = ContextFactory.getLoginUser();
        if (Objects.isNull(user)) {
            return ResponseEntity.ok().build();
        }
        NotificationDto one = notificationService.selectPinnedOne(user.getId());
        return ResponseEntity.ok(NotificationVo.of(one));
    }

    @GetMapping("/list")
    public ResponseEntity<PageVo<NotificationVo>> list(@Validated ListRequest request) {
        User user = ContextFactory.getLoginUser();
        if (Objects.isNull(user)) {
            throw new ForbiddenException();
        }
        IPage<Notification> page = new Page<Notification>()
                .setCurrent(request.getCurrent())
                .setSize(request.getSize());
        IPage<NotificationDto> selectPageDto = notificationService.selectPageDto(page, user.getId());
        return ResponseEntity.ok(PageVo.of(selectPageDto, NotificationVo::of));
    }

    @PostMapping("/reading")
    public ResponseEntity<Void> reading(@RequestBody @Validated ReadingRequest request) {
        User user = ContextFactory.getLoginUser();
        Long notificationId = request.getNotificationId();
        if (Objects.isNull(user)) {
            return ResponseEntity.ok().build();
        }
        if (notificationService.count(Wrappers.query(new Notification().setId(notificationId))) == 0) {
            return ResponseEntity.ok().build();
        }
        NotificationReadMark notificationReadMark = new NotificationReadMark()
                .setNotificationId(notificationId)
                .setUserId(user.getId());
        try {
            notificationReadMarkService.save(notificationReadMark);
        } catch (DuplicateKeyException ignore) {
        }
        return ResponseEntity.ok().build();
    }

    @Data
    public static class ListRequest extends PageRequest {

    }

    @Data
    public static class ReadingRequest {
        @NotNull(message = "消息ID不能为空")
        private Long notificationId;
    }
}
