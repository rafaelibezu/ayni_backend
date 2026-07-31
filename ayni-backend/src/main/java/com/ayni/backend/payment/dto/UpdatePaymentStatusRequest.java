package com.ayni.backend.payment.dto;

import com.ayni.backend.common.enums.PaymentStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UpdatePaymentStatusRequest(

        @NotNull(message = "El estado del pago es obligatorio.")
        PaymentStatus estado,

        @Size(max = 500, message = "La observación no debe superar los 500 caracteres.")
        String observacion
) {
}