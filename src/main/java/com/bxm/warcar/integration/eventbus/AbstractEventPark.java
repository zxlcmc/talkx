package com.bxm.warcar.integration.eventbus;

import com.bxm.warcar.integration.eventbus.core.EventBus;
import com.bxm.warcar.integration.eventbus.core.Subscribe;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.support.AopUtils;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;

import java.lang.reflect.Method;
import java.util.EventObject;
import java.util.Map;
import java.util.Set;

/**
 * @author allen
 * @since 1.0.0
 */
@Slf4j
public abstract class AbstractEventPark implements EventPark, ApplicationListener<ApplicationReadyEvent> {

    protected abstract EventBus getEventBus();
    protected abstract String getEventParkName();

    @Override
    public void register(EventListener listener) {
        EventBus eventBus = getEventBus();
        eventBus.register(listener);
        if (log.isDebugEnabled()) {
            log.debug("Registering listeners to {}: {}", getEventParkName(), listener);
        }

        boolean noSubscribeMethod = true;
        Class<?> clazz = AopUtils.getTargetClass(listener);
        Method[] methods = clazz.getMethods();
        for (Method method : methods) {
            if (method.isAnnotationPresent(Subscribe.class)) {
                noSubscribeMethod = false;
            }
            if (method.isAnnotationPresent(com.google.common.eventbus.Subscribe.class)) {
                throw new RuntimeException(clazz + " Guava @Subscribe does not supported! please use new annotation @com.bxm.warcar.integration.eventbus.core.Subscribe");
            }
        }
        if (noSubscribeMethod) {
            throw new RuntimeException(clazz + " must have an @Subscribe method!");
        }
    }

    @Override
    public void unregister(EventListener listener) {
        getEventBus().unregister(listener);
    }

    @Override
    public void post(EventObject eventObject) {
        getEventBus().post(eventObject);

        if (log.isDebugEnabled()) {
            log.debug("EventBus post event: {}", eventObject);
        }
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        ConfigurableApplicationContext applicationContext = event.getApplicationContext();
        Map<String, EventListener> beansOfType = applicationContext.getBeansOfType(EventListener.class);
        Set<Map.Entry<String, EventListener>> entries = beansOfType.entrySet();
        for (Map.Entry<String, EventListener> entry : entries) {
            this.register(entry.getValue());
        }
    }
}
