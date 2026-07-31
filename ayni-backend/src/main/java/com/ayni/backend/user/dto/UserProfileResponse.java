package com.ayni.backend.user.dto;

import java.time.LocalDateTime;
import java.util.List;

public record UserProfileResponse(
        Long id,
        String nombres,
        String apellidos,
        String email,
        String celular,
        String documentoTipo,
        String documentoNumero,
        Boolean activo,
        Boolean emailVerificado,
        List<String> roles,
        UserLastOrderResponse ultimoPedido,
        LocalDateTime creadoEn
) {
}