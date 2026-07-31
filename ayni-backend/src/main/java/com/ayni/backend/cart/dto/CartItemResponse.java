package com.ayni.backend.cart.dto;

import java.math.BigDecimal;

public record CartItemResponse(
        Long itemId,
        Long productId,
        String nombre,
        String slug,
        String imagenPrincipalUrl,
        Integer cantidad,
        BigDecimal precioUnitario,
        BigDecimal subtotal,
        Integer stock
) {
}