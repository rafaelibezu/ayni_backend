package com.ayni.backend.favorite.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record FavoriteResponse(
        Long id,
        Long productId,
        String nombre,
        String slug,
        BigDecimal precio,
        BigDecimal precioComparacion,
        Integer stock,
        String imagenPrincipalUrl,
        String badge,
        String categoriaNombre,
        String categoriaSlug,
        LocalDateTime creadoEn
) {
}