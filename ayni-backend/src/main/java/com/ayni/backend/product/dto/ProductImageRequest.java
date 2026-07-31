package com.ayni.backend.product.dto;

import jakarta.validation.constraints.*;

public record ProductImageRequest(

        @NotBlank(message = "La URL de imagen es obligatoria.")
        @Size(max = 500, message = "La URL de imagen no debe superar los 500 caracteres.")
        String imagenUrl,

        @Min(value = 1, message = "El orden debe ser mayor o igual a 1.")
        Integer orden
) {
}