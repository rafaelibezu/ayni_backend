package com.ayni.backend.dashboard.service;

import com.ayni.backend.dashboard.dto.AdminDashboardSummaryResponse;
import com.ayni.backend.order.dto.OrderResponse;
import com.ayni.backend.payment.dto.AdminPaymentResponse;

import java.util.List;

public interface AdminDashboardService {

    AdminDashboardSummaryResponse getSummary();

    List<OrderResponse> getRecentOrders();

    List<AdminPaymentResponse> getRecentPayments();
}