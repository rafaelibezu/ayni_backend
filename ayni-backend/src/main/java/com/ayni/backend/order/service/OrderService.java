package com.ayni.backend.order.service;

import com.ayni.backend.order.dto.CheckoutRequest;
import com.ayni.backend.order.dto.OrderResponse;
import com.ayni.backend.order.dto.RegisterYapePaymentRequest;

import java.util.List;

public interface OrderService {

    OrderResponse checkout(CheckoutRequest request);

    OrderResponse registerYapePayment(Long orderId, RegisterYapePaymentRequest request);

    List<OrderResponse> findMyOrders();

    OrderResponse findMyOrderById(Long orderId);

    OrderResponse cancelMyOrder(Long orderId);
}