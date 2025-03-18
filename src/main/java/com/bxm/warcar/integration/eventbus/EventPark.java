package com.bxm.warcar.integration.eventbus;

import java.util.EventObject;

/**
 * @author allen
 * @since 1.0.0
 */
public interface EventPark {

    void post(EventObject eventObject);

    void register(EventListener listener);

    void unregister(EventListener listener);
}
