package org.bigmouth.gpt.mapper.talkx;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;
import org.bigmouth.gpt.entity.Notification;
import org.bigmouth.gpt.entity.NotificationDto;

/**
 * <p>
 * 消息信息 Mapper 接口
 * </p>
 *
 * @author allen
 * @since 2023-10-08
 */
public interface NotificationMapper extends BaseMapper<Notification> {

    IPage<NotificationDto> selectPageDto(IPage<Notification> page, @Param("userId") Long userId);

    NotificationDto selectPinnedOne(@Param("userId") Long userId);
}
