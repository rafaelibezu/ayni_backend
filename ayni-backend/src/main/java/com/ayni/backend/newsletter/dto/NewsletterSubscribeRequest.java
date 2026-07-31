package com.ayni.backend.newsletter.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record NewsletterSubscribeRequest(

        @NotBlank(message = "El correo es obligatorio.")
        @Email(message = "El correo no tiene un formato válido.")
        @Size(max = 150, message = "El correo no debe superar los 150 caracteres.")
        String email,

        @Size(max = 100, message = "El nombre no debe superar los 100 caracteres.")
        String nombres
) {
}