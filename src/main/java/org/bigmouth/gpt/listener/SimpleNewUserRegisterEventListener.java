package org.bigmouth.gpt.listener;

import com.bxm.warcar.integration.eventbus.EventListener;
import com.bxm.warcar.integration.eventbus.core.AllowConcurrentEvents;
import com.bxm.warcar.integration.eventbus.core.Subscribe;
import lombok.extern.slf4j.Slf4j;
import org.bigmouth.gpt.event.NewUserRegisterEvent;
import org.springframework.context.annotation.Configuration;

/**
 * @author huxiao
 * @date 2023/9/25
 * @since 1.0.0
 */
@Slf4j
@Configuration
public class SimpleNewUserRegisterEventListener implements EventListener<NewUserRegisterEvent> {

    @Override
    @Subscribe
    @AllowConcurrentEvents
    public void consume(NewUserRegisterEvent event) {
        log.info("{} 新用户注册成功。", event.getNewUser().getPhoneNum());
    }
}
