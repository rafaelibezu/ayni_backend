package com.ayni.backend.order.service;

import com.ayni.backend.order.dto.OrderResponse;
import com.ayni.backend.order.dto.UpdateOrderStatusRequest;

import java.util.List;

public interface AdminOrderService {

    List<OrderResponse> findAll();

    OrderResponse findById(Long orderId);

    OrderResponse updateStatus(Long orderId, UpdateOrderStatusRequest request);

    OrderResponse cancelOrder(Long orderId);
}