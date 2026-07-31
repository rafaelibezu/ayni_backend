package com.ayni.backend.category.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CategoryRequest(

        @NotBlank(message = "El nombre de la categoría es obligatorio.")
        @Size(max = 100, message = "El nombre no debe superar los 100 caracteres.")
        String nombre,

        @Size(max = 120, message = "El slug no debe superar los 120 caracteres.")
        String slug,

        @Size(max = 255, message = "La descripción no debe superar los 255 caracteres.")
        String descripcion,

        @Size(max = 500, message = "La URL de imagen no debe superar los 500 caracteres.")
        String imagenUrl
) {
}