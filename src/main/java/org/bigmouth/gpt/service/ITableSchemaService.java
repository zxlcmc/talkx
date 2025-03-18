package org.bigmouth.gpt.service;

import org.bigmouth.gpt.entity.TableSchema;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author allen
 * @since 2024-04-15
 */
public interface ITableSchemaService extends IService<TableSchema> {

    long countWithoutDeleted(Long userId);
}
