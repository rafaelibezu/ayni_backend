package com.ayni.backend.product.dto;

import java.math.BigDecimal;
import java.util.List;

public record ProductDetailResponse(
        Long id,
        String nombre,
        String slug,
        String descripcion,
        String sku,
        BigDecimal precio,
        BigDecimal precioComparacion,
        Integer stock,
        String imagenPrincipalUrl,
        String badge,
        Boolean destacado,
        String categoriaNombre,
        String categoriaSlug,
        List<ProductImageResponse> imagenes
) {
}