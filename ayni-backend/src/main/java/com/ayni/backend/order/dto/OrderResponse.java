package com.ayni.backend.order.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record OrderResponse(
        Long id,
        String codigoPedido,
        String estado,
        BigDecimal subtotal,
        BigDecimal costoEnvio,
        BigDecimal total,
        String metodoPago,
        String estadoPago,
        List<OrderItemResponse> items,
        LocalDateTime creadoEn
) {
}