package com.ayni.backend.product.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record ProductRequest(

        @NotNull(message = "La categoría es obligatoria.")
        Long categoryId,

        @NotBlank(message = "El nombre del producto es obligatorio.")
        @Size(max = 150, message = "El nombre no debe superar los 150 caracteres.")
        String nombre,

        @Size(max = 180, message = "El slug no debe superar los 180 caracteres.")
        String slug,

        String descripcion,

        @NotBlank(message = "El SKU es obligatorio.")
        @Size(max = 80, message = "El SKU no debe superar los 80 caracteres.")
        String sku,

        @NotNull(message = "El precio es obligatorio.")
        @DecimalMin(value = "0.0", inclusive = true, message = "El precio no puede ser negativo.")
        BigDecimal precio,

        @DecimalMin(value = "0.0", inclusive = true, message = "El precio comparación no puede ser negativo.")
        BigDecimal precioComparacion,

        @NotNull(message = "El stock es obligatorio.")
        @Min(value = 0, message = "El stock no puede ser negativo.")
        Integer stock,

        @Size(max = 500, message = "La URL de imagen no debe superar los 500 caracteres.")
        String imagenPrincipalUrl,

        @Size(max = 50, message = "El badge no debe superar los 50 caracteres.")
        String badge,

        Boolean destacado
) {
}