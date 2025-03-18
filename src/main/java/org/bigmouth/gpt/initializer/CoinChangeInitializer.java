package org.bigmouth.gpt.initializer;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bxm.warcar.cache.Counter;
import lombok.extern.slf4j.Slf4j;
import org.bigmouth.gpt.entity.User;
import org.bigmouth.gpt.service.CoinService;
import org.bigmouth.gpt.service.IUserService;
import org.bigmouth.gpt.utils.Constants;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author huxiao
 * @date 2023/11/17
 * @since 1.0.0
 */
@Slf4j
@Deprecated
//@Configuration
public class CoinChangeInitializer implements InitializingBean {

    private final Counter counter;
    private final IUserService userService;
    private final CoinService coinService;

    public CoinChangeInitializer(Counter counter, IUserService userService, CoinService coinService) {
        this.counter = counter;
        this.userService = userService;
        this.coinService = coinService;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void afterPropertiesSet() throws Exception {
        Long v = counter.incrementAndGet(() -> "talkx:CoinChangeInitializer");
        if (v > 1) {
            return;
        }

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.gt(User.COIN, 0);
        List<User> users = userService.list(queryWrapper);
        int size = users.size();
        log.info("有蒜粒余额的用户数：{}", size);
        for (User user : users) {
            BigDecimal before = user.getCoin();
            BigDecimal after = before.multiply(BigDecimal.valueOf(0.00333333));
            BigDecimal diff = before.subtract(after);
            coinService.minus(user.getId(), Constants.Coin.BILL_TYPE_RECHARGE, diff, "蒜粒新汇率结转", null);
            log.info("用户：{} 结转前：{} 结转后：{}", user.getId(), before, after);
        }
    }
}
