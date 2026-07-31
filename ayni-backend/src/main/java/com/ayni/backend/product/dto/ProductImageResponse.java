package com.ayni.backend.product.dto;

public record ProductImageResponse(
        Long id,
        String imagenUrl,
        Integer orden
) {
}