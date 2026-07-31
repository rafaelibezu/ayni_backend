package com.ayni.backend.order.dto;

import jakarta.validation.constraints.Size;

public record CheckoutRequest(

        Long addressId,

        @Size(max = 120, message = "El destinatario no debe superar los 120 caracteres.")
        String destinatario,

        @Size(max = 20, message = "El celular no debe superar los 20 caracteres.")
        String celular,

        @Size(max = 20, message = "El tipo de documento no debe superar los 20 caracteres.")
        String documentoTipo,

        @Size(max = 20, message = "El número de documento no debe superar los 20 caracteres.")
        String documentoNumero,

        @Size(max = 255, message = "La dirección no debe superar los 255 caracteres.")
        String direccion,

        @Size(max = 100, message = "El distrito no debe superar los 100 caracteres.")
        String distrito,

        @Size(max = 255, message = "La referencia no debe superar los 255 caracteres.")
        String referencia,

        @Size(max = 500, message = "Las observaciones no deben superar los 500 caracteres.")
        String observaciones
) {
}