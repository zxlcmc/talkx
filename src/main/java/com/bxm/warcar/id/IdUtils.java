package com.bxm.warcar.id;

import org.apache.commons.lang3.StringUtils;

import java.util.Calendar;

/**
 * @author allen
 * @date 2022-03-22
 * @since 1.0
 */
public class IdUtils {

    private IdUtils() {}

    public static String getDateTime() {
        // 比 DateTimeFormatter 性能高。
        Calendar calendar = Calendar.getInstance();
        return StringUtils.join(new Object[] {
                calendar.get(Calendar.YEAR),
                StringUtils.leftPad(String.valueOf(calendar.get(Calendar.MONTH) + 1), 2, '0'),
                StringUtils.leftPad(String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)), 2, '0'),
                StringUtils.leftPad(String.valueOf(calendar.get(Calendar.HOUR_OF_DAY)), 2, '0'),
                StringUtils.leftPad(String.valueOf(calendar.get(Calendar.MINUTE)), 2, '0'),
                StringUtils.leftPad(String.valueOf(calendar.get(Calendar.SECOND)), 2, '0'),
                StringUtils.leftPad(String.valueOf(calendar.get(Calendar.MILLISECOND)), 3, '0')
        });
    }
}
