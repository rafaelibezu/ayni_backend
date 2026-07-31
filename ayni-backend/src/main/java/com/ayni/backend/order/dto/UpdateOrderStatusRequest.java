package com.ayni.backend.order.dto;

import com.ayni.backend.common.enums.OrderStatus;
import jakarta.validation.constraints.NotNull;

public record UpdateOrderStatusRequest(

        @NotNull(message = "El estado del pedido es obligatorio.")
        OrderStatus estado
) {
}