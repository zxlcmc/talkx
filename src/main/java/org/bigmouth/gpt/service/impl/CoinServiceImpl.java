package org.bigmouth.gpt.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.bigmouth.gpt.entity.CoinBill;
import org.bigmouth.gpt.entity.CoinCatalog;
import org.bigmouth.gpt.service.CoinService;
import org.bigmouth.gpt.service.ICoinBillService;
import org.bigmouth.gpt.service.ICoinCatalogService;
import org.bigmouth.gpt.service.IUserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author huxiao
 * @date 2023/9/20
 * @since 1.0.0
 */
@Service
public class CoinServiceImpl implements CoinService {

    private final IUserService userService;
    private final ICoinBillService coinBillService;
    private final ICoinCatalogService coinCatalogService;

    public CoinServiceImpl(IUserService userService, ICoinBillService coinBillService, ICoinCatalogService coinCatalogService) {
        this.userService = userService;
        this.coinBillService = coinBillService;
        this.coinCatalogService = coinCatalogService;
    }

    @Override
    public List<CoinCatalog> getCoinCatalog() {
        QueryWrapper<CoinCatalog> queryWrapper = new QueryWrapper<>();
        queryWrapper.ge(CoinCatalog.STOCKS, 0);
        queryWrapper.orderByAsc(CoinCatalog.PRICE);
        return coinCatalogService.list(queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean plus(long userId, int type, BigDecimal value, String title) {
        return plus(userId, type, value, title, null);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean plus(long userId, int type, BigDecimal value, String title, String desc) {
        if (userService.plusCoin(userId, value)) {
            CoinBill coinBill = new CoinBill()
                    .setUserId(userId)
                    .setValue(value)
                    .setType(type)
                    .setBillTitle(title)
                    .setBillDesc(desc)
                    .setCoin(userService.getCoin(userId));
            coinBillService.save(coinBill);
            return true;
        }
        return false;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean minus(long userId, int type, BigDecimal value, String title, String desc) {
        if (userService.minusCoin(userId, value)) {
            CoinBill coinBill = new CoinBill()
                    .setUserId(userId)
                    .setValue(value.negate())
                    .setType(type)
                    .setBillTitle(title)
                    .setBillDesc(desc)
                    .setCoin(userService.getCoin(userId));
            coinBillService.save(coinBill);
            return true;
        }
        return false;
    }

    @Override
    public BigDecimal sumByDate(String date, int type) {
        return coinBillService.sumByDate(date, type);
    }
}
