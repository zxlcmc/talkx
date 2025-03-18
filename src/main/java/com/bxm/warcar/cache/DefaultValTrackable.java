/*
 * Copyright 2016 bianxianmao.com All right reserved. This software is the confidential and proprietary information of
 * textile.com ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only
 * in accordance with the terms of the license agreement you entered into with bianxianmao.com.
 */
package com.bxm.warcar.cache;

/**
 * 初始值追踪器
 *
 * @author allen
 * @date 2017-12-05
 */
public interface DefaultValTrackable {

    /**
     * 返回初始值
     *
     * @return
     */
    long getDefaultVal();
}
