package org.bigmouth.gpt.service.impl;

import org.bigmouth.gpt.entity.SessionMessage;
import org.bigmouth.gpt.mapper.talkx.SessionMessageMapper;
import org.bigmouth.gpt.service.ISessionMessageService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Allen Hu
 * @since 2023-06-12
 */
@Service
public class SessionMessageServiceImpl extends ServiceImpl<SessionMessageMapper, SessionMessage> implements ISessionMessageService {

}
