package org.bigmouth.gpt.entity.map;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.bigmouth.gpt.entity.TableSchema;
import org.bigmouth.gpt.entity.TableSchemaVo;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Optional;

/**
 * @author huxiao
 * @date 2024/4/16
 * @since 1.0.0
 */
@Mapper
public interface TableSchemaMapper {

    TableSchemaMapper INSTANCE = Mappers.getMapper(TableSchemaMapper.class);

    @Mapping(target = "conversationStart", expression = "java(split(o.getConversactionStart()))")
    TableSchemaVo of(TableSchema o);

    @IterableMapping(elementTargetType = TableSchemaVo.class)
    List<TableSchemaVo> of(List<TableSchema> list);

    default List<String> split(String conversationStart) {
        return Optional.ofNullable(conversationStart)
                .filter(StringUtils::isNotBlank)
                .map(e -> Lists.newArrayList(StringUtils.split(e, ",")))
                .orElse(null);
    }
}
