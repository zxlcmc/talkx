package com.bxm.warcar.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.UUID;

/**
 * @author allen
 * @since 1.0.0
 */
public final class UUIDHelper {

    public static String generate() {
        return StringUtils.replace(UUID.randomUUID().toString(), "-", "");
    }
}
