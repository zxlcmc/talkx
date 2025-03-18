/*
 * Copyright 2016 bianxianmao.com All right reserved. This software is the confidential and proprietary information of
 * textile.com ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with bianxianmao.com.
 */

package com.bxm.warcar.cache.serialization;

import java.util.List;
import java.util.Map;

/**
 *
 * @author allen
 */
public interface Serialization<MODAL> {

    MODAL serialize(Object data);

    <T> T deserialize(MODAL modalValue, Class<T> cls);

    <T> List<T> deserializeList(MODAL modalValue, Class<T> cls);

    <T> Map<String, T> deserializeMap(MODAL modalValue, Class<T> cls);
}
