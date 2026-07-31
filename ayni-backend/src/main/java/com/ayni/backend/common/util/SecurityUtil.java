package com.ayni.backend.common.util;

import com.ayni.backend.security.userdetail.CustomUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public final class SecurityUtil {

    private SecurityUtil() {
    }

    public static CustomUserDetails getCurrentUser() {
        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();

        if (authentication == null || !(authentication.getPrincipal() instanceof CustomUserDetails)) {
            throw new IllegalStateException("Usuario no autenticado.");
        }

        return (CustomUserDetails) authentication.getPrincipal();
    }

    public static String getCurrentUserEmail() {
        return getCurrentUser().getEmail();
    }

    public static Long getCurrentUserId() {
        return getCurrentUser().getId();
    }
}