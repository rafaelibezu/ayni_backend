package com.ayni.backend.order.dto;

import java.time.LocalDateTime;

public record AddressResponse(
        Long id,
        String destinatario,
        String celular,
        String documentoTipo,
        String documentoNumero,
        String direccion,
        String distrito,
        String referencia,
        Boolean esPrincipal,
        LocalDateTime creadoEn
) {
}