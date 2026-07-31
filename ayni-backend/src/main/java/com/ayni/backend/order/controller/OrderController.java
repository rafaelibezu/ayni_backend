package com.ayni.backend.order.controller;

import com.ayni.backend.common.constants.ApiRoutes;
import com.ayni.backend.common.response.ApiResponse;
import com.ayni.backend.order.dto.CheckoutRequest;
import com.ayni.backend.order.dto.OrderResponse;
import com.ayni.backend.order.dto.RegisterYapePaymentRequest;
import com.ayni.backend.order.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping(ApiRoutes.ORDERS + "/checkout")
    public ApiResponse<OrderResponse> checkout(
            @Valid @RequestBody CheckoutRequest request
    ) {
        return ApiResponse.ok(
                "Pedido creado correctamente.",
                orderService.checkout(request)
        );
    }

    @PostMapping(ApiRoutes.ORDERS + "/{orderId}/yape-payment")
    public ApiResponse<OrderResponse> registerYapePayment(
            @PathVariable Long orderId,
            @Valid @RequestBody RegisterYapePaymentRequest request
    ) {
        return ApiResponse.ok(
                "Pago Yape registrado correctamente.",
                orderService.registerYapePayment(orderId, request)
        );
    }

    @GetMapping(ApiRoutes.ORDERS + "/my")
    public ApiResponse<List<OrderResponse>> findMyOrders() {
        return ApiResponse.ok(
                "Pedidos listados correctamente.",
                orderService.findMyOrders()
        );
    }

    @GetMapping(ApiRoutes.ORDERS + "/{orderId}")
    public ApiResponse<OrderResponse> findMyOrderById(
            @PathVariable Long orderId
    ) {
        return ApiResponse.ok(
                "Pedido obtenido correctamente.",
                orderService.findMyOrderById(orderId)
        );
    }

    @PatchMapping(ApiRoutes.ORDERS + "/{orderId}/cancel")
    public ApiResponse<OrderResponse> cancelMyOrder(
            @PathVariable Long orderId
    ) {
        return ApiResponse.ok(
                "Pedido cancelado correctamente.",
                orderService.cancelMyOrder(orderId)
        );
    }
}