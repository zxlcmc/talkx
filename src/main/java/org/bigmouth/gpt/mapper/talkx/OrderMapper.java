package org.bigmouth.gpt.mapper.talkx;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Param;
import org.bigmouth.gpt.entity.Order;

import java.math.BigDecimal;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author allen
 * @since 2023-11-03
 */
public interface OrderMapper extends BaseMapper<Order> {

    BigDecimal sum(@Param(Constants.WRAPPER) Wrapper<Order> queryWrapper);
}
