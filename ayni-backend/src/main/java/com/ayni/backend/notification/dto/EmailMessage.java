package com.ayni.backend.notification.dto;

public record EmailMessage(
        String to,
        String subject,
        String body
) {
}