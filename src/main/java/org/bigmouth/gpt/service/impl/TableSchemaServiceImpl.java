package org.bigmouth.gpt.service.impl;

import org.bigmouth.gpt.entity.TableSchema;
import org.bigmouth.gpt.mapper.talkx.TableSchemaMapper;
import org.bigmouth.gpt.service.ITableSchemaService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author allen
 * @since 2024-04-15
 */
@Service
public class TableSchemaServiceImpl extends ServiceImpl<TableSchemaMapper, TableSchema> implements ITableSchemaService {
    @Override
    public long countWithoutDeleted(Long userId) {
        return getBaseMapper().countWithoutDeleted(userId);
    }
}
