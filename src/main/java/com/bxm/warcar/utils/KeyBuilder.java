/*
 * Copyright 2016 bianxianmao.com All right reserved. This software is the confidential and proprietary information of
 * textile.com ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with bianxianmao.com.
 */

package com.bxm.warcar.utils;

import org.apache.commons.lang3.StringUtils;

/**
 * @author allen
 * @date 2017-12-04
 */
public final class KeyBuilder {

    public static final char SPLIT = ':';

    private KeyBuilder() {}

    public static String build(Object...array) {
        return StringUtils.join(array, SPLIT);
    }
}
