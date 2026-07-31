package com.ayni.backend.payment.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record AdminPaymentResponse(
        Long id,
        Long orderId,
        String codigoPedido,
        String metodo,
        String estado,
        BigDecimal monto,
        String numeroOperacion,
        String comprobanteUrl,
        String observacionAdmin,
        LocalDateTime creadoEn,
        LocalDateTime validadoEn
) {
}