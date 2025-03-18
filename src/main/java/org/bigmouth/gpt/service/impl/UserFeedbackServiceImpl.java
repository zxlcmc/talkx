package org.bigmouth.gpt.service.impl;

import org.bigmouth.gpt.entity.UserFeedback;
import org.bigmouth.gpt.mapper.talkx.UserFeedbackMapper;
import org.bigmouth.gpt.service.IUserFeedbackService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户反馈 服务实现类
 * </p>
 *
 * @author tangxiao
 * @since 2023-07-21
 */
@Service
public class UserFeedbackServiceImpl extends ServiceImpl<UserFeedbackMapper, UserFeedback> implements IUserFeedbackService {

}
