package com.ayni.backend.common.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class DateUtil {

    private DateUtil() {
    }

    public static LocalDateTime now() {
        return LocalDateTime.now();
    }

    public static String timestampForCode() {
        return LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
    }
}