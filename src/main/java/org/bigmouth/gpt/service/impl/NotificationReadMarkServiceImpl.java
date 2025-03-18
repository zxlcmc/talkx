package org.bigmouth.gpt.service.impl;

import org.bigmouth.gpt.entity.NotificationReadMark;
import org.bigmouth.gpt.mapper.talkx.NotificationReadMarkMapper;
import org.bigmouth.gpt.service.INotificationReadMarkService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 消息阅读标记 服务实现类
 * </p>
 *
 * @author allen
 * @since 2023-10-08
 */
@Service
public class NotificationReadMarkServiceImpl extends ServiceImpl<NotificationReadMarkMapper, NotificationReadMark> implements INotificationReadMarkService {

}
