package com.ayni.backend.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateUserProfileRequest(

        @NotBlank(message = "El nombre es obligatorio.")
        @Size(max = 100, message = "El nombre no debe superar los 100 caracteres.")
        String nombres,

        @Size(max = 100, message = "Los apellidos no deben superar los 100 caracteres.")
        String apellidos,

        @Size(max = 20, message = "El celular no debe superar los 20 caracteres.")
        String celular,

        @Size(max = 20, message = "El tipo de documento no debe superar los 20 caracteres.")
        String documentoTipo,

        @Size(max = 20, message = "El número de documento no debe superar los 20 caracteres.")
        String documentoNumero
) {
}