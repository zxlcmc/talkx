package org.bigmouth.gpt.service;

import org.bigmouth.gpt.entity.Attachment;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 附件表 服务类
 * </p>
 *
 * @author allen
 * @since 2024-01-18
 */
public interface IAttachmentService extends IService<Attachment> {

    Attachment getByIdWithCache(Long id);
}
