package com.ayni.backend.order.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AddressRequest(

        @NotBlank(message = "El destinatario es obligatorio.")
        @Size(max = 120, message = "El destinatario no debe superar los 120 caracteres.")
        String destinatario,

        @NotBlank(message = "El celular es obligatorio.")
        @Size(max = 20, message = "El celular no debe superar los 20 caracteres.")
        String celular,

        @Size(max = 20, message = "El tipo de documento no debe superar los 20 caracteres.")
        String documentoTipo,

        @Size(max = 20, message = "El número de documento no debe superar los 20 caracteres.")
        String documentoNumero,

        @NotBlank(message = "La dirección es obligatoria.")
        @Size(max = 255, message = "La dirección no debe superar los 255 caracteres.")
        String direccion,

        @NotBlank(message = "El distrito es obligatorio.")
        @Size(max = 100, message = "El distrito no debe superar los 100 caracteres.")
        String distrito,

        @Size(max = 255, message = "La referencia no debe superar los 255 caracteres.")
        String referencia,

        Boolean esPrincipal
) {
}