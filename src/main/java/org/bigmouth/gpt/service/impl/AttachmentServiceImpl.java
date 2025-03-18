package org.bigmouth.gpt.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bxm.warcar.cache.Fetcher;
import org.bigmouth.gpt.entity.Attachment;
import org.bigmouth.gpt.mapper.talkx.AttachmentMapper;
import org.bigmouth.gpt.service.IAttachmentService;
import org.bigmouth.gpt.utils.RedisKeys;
import org.springframework.stereotype.Service;

import java.time.Duration;

/**
 * <p>
 * 附件表 服务实现类
 * </p>
 *
 * @author allen
 * @since 2024-01-18
 */
@Service
public class AttachmentServiceImpl extends ServiceImpl<AttachmentMapper, Attachment> implements IAttachmentService {
    private final Fetcher fetcher;

    public AttachmentServiceImpl(Fetcher fetcher) {
        this.fetcher = fetcher;
    }

    @Override
    public Attachment getByIdWithCache(Long id) {
        return fetcher.fetch(RedisKeys.Attachment.stringAttachment(id), () -> getById(id), Attachment.class, (int) Duration.ofDays(1).getSeconds());
    }
}
