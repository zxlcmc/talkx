package org.bigmouth.gpt.service.impl;

import org.bigmouth.gpt.entity.CoinBill;
import org.bigmouth.gpt.mapper.talkx.CoinBillMapper;
import org.bigmouth.gpt.service.ICoinBillService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * <p>
 * 蒜粒账单 服务实现类
 * </p>
 *
 * @author allen
 * @since 2023-09-19
 */
@Service
public class CoinBillServiceImpl extends ServiceImpl<CoinBillMapper, CoinBill> implements ICoinBillService {
    @Override
    public BigDecimal sumByDate(String date, int type) {
        return getBaseMapper().sumByDate(date, type);
    }
}
