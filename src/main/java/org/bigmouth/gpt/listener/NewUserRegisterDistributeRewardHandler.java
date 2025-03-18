package org.bigmouth.gpt.listener;

import com.bxm.warcar.integration.eventbus.EventListener;
import com.bxm.warcar.integration.eventbus.core.AllowConcurrentEvents;
import com.bxm.warcar.integration.eventbus.core.Subscribe;
import lombok.extern.slf4j.Slf4j;
import org.bigmouth.gpt.ApplicationConfig;
import org.bigmouth.gpt.entity.User;
import org.bigmouth.gpt.event.NewUserRegisterEvent;
import org.bigmouth.gpt.service.CoinService;
import org.bigmouth.gpt.utils.Constants;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * @author huxiao
 * @date 2023/9/20
 * @since 1.0.0
 */
@Slf4j
@Configuration
public class NewUserRegisterDistributeRewardHandler implements EventListener<NewUserRegisterEvent> {

    private final ApplicationConfig applicationConfig;
    private final CoinService coinService;

    public NewUserRegisterDistributeRewardHandler(ApplicationConfig applicationConfig, CoinService coinService) {
        this.applicationConfig = applicationConfig;
        this.coinService = coinService;
    }

    @Override
    @Subscribe
    @AllowConcurrentEvents
    @Transactional(rollbackFor = Exception.class, timeout = 30)
    public void consume(NewUserRegisterEvent event) {
        User newUser = event.getNewUser();
        BigDecimal registerRewardCoins = applicationConfig.getRegisterRewardCoins();
        if (registerRewardCoins.doubleValue() > 0) {
            coinService.plus(newUser.getId(), Constants.Coin.BILL_TYPE_REWARDS, registerRewardCoins, "注册奖励");
            log.info("{} 发放注册奖励 {}", newUser.getPhoneNum(), registerRewardCoins);
        }

        User inviter = event.getInviter();
        if (Objects.isNull(inviter)) {
            return;
        }
        BigDecimal value = applicationConfig.getInviteRegisterRewardCoins();
        if (value.doubleValue() <= 0) {
            return;
        }
        coinService.plus(inviter.getId(), Constants.Coin.BILL_TYPE_REWARDS, value, "邀请注册奖励");
        log.info("{} 邀请 {} 注册发放奖励 {}", inviter.getPhoneNum(), newUser.getPhoneNum(), value);
        coinService.plus(newUser.getId(), Constants.Coin.BILL_TYPE_REWARDS, value, "邀请注册奖励");
        log.info("{} 被邀请发放注册奖励 {}", newUser.getPhoneNum(), value);
    }
}
