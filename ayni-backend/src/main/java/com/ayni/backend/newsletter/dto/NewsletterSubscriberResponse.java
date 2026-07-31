package com.ayni.backend.newsletter.dto;

import java.time.LocalDateTime;

public record NewsletterSubscriberResponse(
        Long id,
        String email,
        String nombres,
        Boolean activo,
        LocalDateTime creadoEn
) {
}