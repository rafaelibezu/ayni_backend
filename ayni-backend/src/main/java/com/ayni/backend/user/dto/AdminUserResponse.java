package com.ayni.backend.user.dto;

import java.time.LocalDateTime;
import java.util.Set;

public record AdminUserResponse(
        Long id,
        String nombres,
        String apellidos,
        String email,
        String celular,
        String documentoTipo,
        String documentoNumero,
        Boolean activo,
        Boolean emailVerificado,
        String proveedorAuth,
        Set<String> roles,
        LocalDateTime creadoEn
) {
}