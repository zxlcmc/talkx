package com.bxm.warcar.utils;

import com.google.common.collect.Maps;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

import java.util.Collection;
import java.util.Map;

/**
 * 抽象的Bean总线。可以在应用准备好之后映射所匹配的实例。
 * @author allen
 * @date 2021-05-24
 * @since 1.0
 */
public abstract class AbstractBeanBus<K, S> implements BeanPostProcessor {

    private final Map<K, S> mapping = Maps.newHashMap();

    /**
     * 返回实例类型
     * @return 类型
     */
    protected abstract Class<S> getInstanceClazz();

    /**
     * 返回这个 {@code bean} 映射的主键，可通过该主键获取到实例。
     * @param beanName Spring 装载后的bean name
     * @param bean 实例
     * @return 主键
     */
    protected abstract K getKey(String beanName, S bean);

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (getInstanceClazz().isInstance(bean)) {
            S o = (S) bean;
            K key = getKey(beanName, o);
            mapping.put(key, o);
        }
        return bean;
    }

    public S get(K key) {
        return mapping.get(key);
    }

    public Collection<S> getAll() {
        return mapping.values();
    }
}
