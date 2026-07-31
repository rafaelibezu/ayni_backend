package com.ayni.backend.common.util;

public final class GlobalUtil {

    private GlobalUtil() {
    }

    public static boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

    public static String normalizeEmail(String email) {
        if (email == null) {
            return null;
        }

        return email.trim().toLowerCase();
    }

    public static String cleanText(String value) {
        if (value == null) {
            return null;
        }

        return value.trim();
    }
}