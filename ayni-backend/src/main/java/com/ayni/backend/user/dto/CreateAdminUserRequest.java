package com.ayni.backend.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateAdminUserRequest(

        @NotBlank(message = "Los nombres son obligatorios.")
        @Size(max = 100, message = "Los nombres no deben superar los 100 caracteres.")
        String nombres,

        @NotBlank(message = "Los apellidos son obligatorios.")
        @Size(max = 100, message = "Los apellidos no deben superar los 100 caracteres.")
        String apellidos,

        @NotBlank(message = "El email es obligatorio.")
        @Email(message = "El email no tiene un formato válido.")
        @Size(max = 150, message = "El email no debe superar los 150 caracteres.")
        String email,

        @NotBlank(message = "La contraseña es obligatoria.")
        @Size(min = 8, max = 100, message = "La contraseña debe tener entre 8 y 100 caracteres.")
        String password,

        @Size(max = 20, message = "El celular no debe superar los 20 caracteres.")
        String celular,

        @Size(max = 20, message = "El tipo de documento no debe superar los 20 caracteres.")
        String documentoTipo,

        @Size(max = 20, message = "El número de documento no debe superar los 20 caracteres.")
        String documentoNumero
) {
}