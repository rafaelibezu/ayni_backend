package com.ayni.backend.order.dto;

import jakarta.validation.constraints.NotBlank;

public record RegisterYapePaymentRequest(

        @NotBlank(message = "El número de operación es obligatorio.")
        String numeroOperacion,

        String comprobanteUrl
) {
}