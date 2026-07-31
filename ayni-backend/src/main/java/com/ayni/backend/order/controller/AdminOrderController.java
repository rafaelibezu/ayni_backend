package com.ayni.backend.order.controller;

import com.ayni.backend.common.constants.ApiRoutes;
import com.ayni.backend.common.response.ApiResponse;
import com.ayni.backend.order.dto.OrderResponse;
import com.ayni.backend.order.dto.UpdateOrderStatusRequest;
import com.ayni.backend.order.service.AdminOrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class AdminOrderController {

    private final AdminOrderService adminOrderService;

    @PutMapping(ApiRoutes.ADMIN + "/orders/{orderId}/status")
    public ApiResponse<OrderResponse> updateStatus(
            @PathVariable Long orderId,
            @Valid @RequestBody UpdateOrderStatusRequest request
    ) {
        return ApiResponse.ok(
                "Estado del pedido actualizado correctamente.",
                adminOrderService.updateStatus(orderId, request)
        );
    }

    @GetMapping(ApiRoutes.ADMIN + "/orders")
    public ApiResponse<List<OrderResponse>> findAll() {
        return ApiResponse.ok(
                "Pedidos obtenidos correctamente.",
                adminOrderService.findAll()
        );
    }

    /*Admin*/
    @GetMapping(ApiRoutes.ADMIN + "/orders/{orderId}")
    public ApiResponse<OrderResponse> findById(
            @PathVariable Long orderId
    ) {
        return ApiResponse.ok(
                "Pedido obtenido correctamente.",
                adminOrderService.findById(orderId)
        );
    }

    @PatchMapping(ApiRoutes.ADMIN + "/orders/{orderId}/cancel")
    public ApiResponse<OrderResponse> cancelOrder(
            @PathVariable Long orderId
    ) {
        return ApiResponse.ok(
                "Pedido cancelado correctamente.",
                adminOrderService.cancelOrder(orderId)
        );
    }
}