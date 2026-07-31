package com.ayni.backend.user.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record UserLastOrderResponse(
        Long id,
        String codigoPedido,
        String estado,
        BigDecimal total,
        LocalDateTime creadoEn
) {
}