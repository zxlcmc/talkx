package org.bigmouth.gpt.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.bigmouth.gpt.entity.Notification;
import org.bigmouth.gpt.entity.NotificationDto;

/**
 * <p>
 * 消息信息 服务类
 * </p>
 *
 * @author allen
 * @since 2023-10-08
 */
public interface INotificationService extends IService<Notification> {

    IPage<NotificationDto> selectPageDto(IPage<Notification> page, Long userId);

    NotificationDto selectPinnedOne(Long userId);
}
