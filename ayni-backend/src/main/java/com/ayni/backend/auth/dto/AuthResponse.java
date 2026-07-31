package com.ayni.backend.auth.dto;

import java.util.List;

public record AuthResponse(
        Long id,
        String nombres,
        String apellidos,
        String email,
        String token,
        List<String> roles
) {
}