package com.ayni.backend.order.dto;

import java.math.BigDecimal;

public record OrderItemResponse(
        Long id,
        String nombreProducto,
        String sku,
        Integer cantidad,
        BigDecimal precioUnitario,
        BigDecimal subtotal
) {
}