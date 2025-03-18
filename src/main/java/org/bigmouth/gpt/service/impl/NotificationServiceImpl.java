package org.bigmouth.gpt.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.bigmouth.gpt.entity.Notification;
import org.bigmouth.gpt.entity.NotificationDto;
import org.bigmouth.gpt.mapper.talkx.NotificationMapper;
import org.bigmouth.gpt.service.INotificationService;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 消息信息 服务实现类
 * </p>
 *
 * @author allen
 * @since 2023-10-08
 */
@Service
public class NotificationServiceImpl extends ServiceImpl<NotificationMapper, Notification> implements INotificationService {
    @Override
    public IPage<NotificationDto> selectPageDto(IPage<Notification> page, Long userId) {
        return getBaseMapper().selectPageDto(page, userId);
    }

    @Override
    public NotificationDto selectPinnedOne(Long userId) {
        return getBaseMapper().selectPinnedOne(userId);
    }
}
