package com.ayni.backend.category.dto;

public record CategoryResponse(
        Long id,
        String nombre,
        String slug,
        String descripcion,
        String imagenUrl,
        Boolean activo
) {
}