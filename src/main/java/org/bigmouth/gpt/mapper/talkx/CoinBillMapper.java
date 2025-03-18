package org.bigmouth.gpt.mapper.talkx;

import org.apache.ibatis.annotations.Param;
import org.bigmouth.gpt.entity.CoinBill;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.math.BigDecimal;

/**
 * <p>
 * 蒜粒账单 Mapper 接口
 * </p>
 *
 * @author allen
 * @since 2023-09-19
 */
public interface CoinBillMapper extends BaseMapper<CoinBill> {

    BigDecimal sumByDate(@Param("date") String date,
                         @Param("type") Integer type);
}
