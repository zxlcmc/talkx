package org.bigmouth.gpt.mapper.talkx;

import org.bigmouth.gpt.entity.TableSchema;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author allen
 * @since 2024-04-15
 */
public interface TableSchemaMapper extends BaseMapper<TableSchema> {

    long countWithoutDeleted(Long userId);
}
