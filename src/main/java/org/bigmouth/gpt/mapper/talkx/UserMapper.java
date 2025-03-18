package org.bigmouth.gpt.mapper.talkx;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.bigmouth.gpt.entity.User;

import java.math.BigDecimal;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Allen Hu
 * @since 2023-06-12
 */
public interface UserMapper extends BaseMapper<User> {

    BigDecimal getCoin(@Param("id") long id);

    int plusCoin(@Param("id") long id, @Param("value") BigDecimal value);

    int minusCoin(@Param("id") long id, @Param("value") BigDecimal value);
}
