package com.ayni.backend.order.mapper;

import com.ayni.backend.order.dto.OrderItemResponse;
import com.ayni.backend.order.dto.OrderResponse;
import com.ayni.backend.order.entity.Order;
import com.ayni.backend.order.entity.OrderDetail;
import com.ayni.backend.payment.entity.Payment;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderMapper {

    public OrderItemResponse toItemResponse(OrderDetail detail) {
        return new OrderItemResponse(
                detail.getId(),
                detail.getNombreProducto(),
                detail.getSku(),
                detail.getCantidad(),
                detail.getPrecioUnitario(),
                detail.getSubtotal()
        );
    }

    public OrderResponse toResponse(
            Order order,
            List<OrderDetail> details,
            Payment payment
    ) {
        List<OrderItemResponse> items = details.stream()
                .map(this::toItemResponse)
                .toList();

        return new OrderResponse(
                order.getId(),
                order.getCodigoPedido(),
                order.getEstado().name(),
                order.getSubtotal(),
                order.getCostoEnvio(),
                order.getTotal(),
                payment != null ? payment.getMetodo().name() : null,
                payment != null ? payment.getEstado().name() : null,
                items,
                order.getCreadoEn()
        );
    }
}