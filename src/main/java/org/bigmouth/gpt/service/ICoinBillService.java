package org.bigmouth.gpt.service;

import org.bigmouth.gpt.entity.CoinBill;
import com.baomidou.mybatisplus.extension.service.IService;

import java.math.BigDecimal;

/**
 * <p>
 * 蒜粒账单 服务类
 * </p>
 *
 * @author allen
 * @since 2023-09-19
 */
public interface ICoinBillService extends IService<CoinBill> {
    /**
     * 统计蒜粒
     *
     * @param date 日期。yyyy-MM-dd
     * @param type 类型
     * @return 蒜粒合计
     */
    BigDecimal sumByDate(String date, int type);
}
