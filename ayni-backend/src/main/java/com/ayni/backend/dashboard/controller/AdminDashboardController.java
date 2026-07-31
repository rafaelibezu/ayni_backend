package com.ayni.backend.dashboard.controller;

import com.ayni.backend.common.constants.ApiRoutes;
import com.ayni.backend.common.response.ApiResponse;
import com.ayni.backend.dashboard.dto.AdminDashboardSummaryResponse;
import com.ayni.backend.dashboard.service.AdminDashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ayni.backend.order.dto.OrderResponse;
import com.ayni.backend.payment.dto.AdminPaymentResponse;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AdminDashboardController {

    private final AdminDashboardService adminDashboardService;

    @GetMapping(ApiRoutes.ADMIN + "/dashboard/summary")
    public ApiResponse<AdminDashboardSummaryResponse> getSummary() {
        return ApiResponse.ok(
                "Resumen del dashboard obtenido correctamente.",
                adminDashboardService.getSummary()
        );
    }

    @GetMapping(ApiRoutes.ADMIN + "/dashboard/recent-orders")
    public ApiResponse<List<OrderResponse>> getRecentOrders() {
        return ApiResponse.ok(
                "Últimos pedidos obtenidos correctamente.",
                adminDashboardService.getRecentOrders()
        );
    }

    @GetMapping(ApiRoutes.ADMIN + "/dashboard/recent-payments")
    public ApiResponse<List<AdminPaymentResponse>> getRecentPayments() {
        return ApiResponse.ok(
                "Últimos pagos obtenidos correctamente.",
                adminDashboardService.getRecentPayments()
        );
    }
}