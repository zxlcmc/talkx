package org.bigmouth.gpt.service;

import org.bigmouth.gpt.entity.CoinCatalog;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author huxiao
 * @date 2023/9/20
 * @since 1.0.0
 */
public interface CoinService {

    /**
     * 获取蒜粒目录价
     * @return 目录价
     */
    List<CoinCatalog> getCoinCatalog();

    /**
     * 为指定用户添加蒜粒
     * @param userId 用户ID
     * @param type 类型。
     *             {@link org.bigmouth.gpt.utils.Constants.Coin#BILL_TYPE_RECHARGE 充值}、
     *             {@link org.bigmouth.gpt.utils.Constants.Coin#BILL_TYPE_REWARDS 奖励}
     * @param value 添加的蒜粒数量
     * @param title 标题
     * @return 是否成功
     */
    boolean plus(long userId, int type, BigDecimal value, String title);

    /**
     * 为指定用户添加蒜粒
     * @param userId 用户ID
     * @param type 类型。
     *             {@link org.bigmouth.gpt.utils.Constants.Coin#BILL_TYPE_RECHARGE 充值}、
     *             {@link org.bigmouth.gpt.utils.Constants.Coin#BILL_TYPE_REWARDS 奖励}
     * @param value 添加的蒜粒数量
     * @param title 标题
     * @param desc 描述
     * @return 是否成功
     */
    boolean plus(long userId, int type, BigDecimal value, String title, String desc);

    /**
     * 减少指定用户的蒜粒
     * @param userId 用户ID
     * @param type 类型
     *             {@link org.bigmouth.gpt.utils.Constants.Coin#BILL_TYPE_USE 消耗}
     * @param value 蒜粒数量
     * @param title 标题
     * @param desc 描述
     * @return 是否成功
     */
    boolean minus(long userId, int type, BigDecimal value, String title, String desc);

    /**
     * 统计蒜粒
     *
     * @param date 日期。yyyy-MM-dd
     * @param type 类型
     * @return 蒜粒合计
     */
    BigDecimal sumByDate(String date, int type);
}
