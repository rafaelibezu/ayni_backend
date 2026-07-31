package com.ayni.backend.dashboard.service;

import com.ayni.backend.category.repository.CategoryRepository;
import com.ayni.backend.common.enums.*;
import com.ayni.backend.dashboard.dto.AdminDashboardSummaryResponse;
import com.ayni.backend.order.dto.OrderResponse;
import com.ayni.backend.order.entity.OrderDetail;
import com.ayni.backend.order.mapper.OrderMapper;
import com.ayni.backend.order.repository.*;
import com.ayni.backend.payment.dto.AdminPaymentResponse;
import com.ayni.backend.payment.entity.*;
import com.ayni.backend.payment.repository.*;
import com.ayni.backend.product.repository.ProductRepository;
import com.ayni.backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminDashboardServiceImpl implements AdminDashboardService {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;

    private final OrderDetailRepository orderDetailRepository;
    private final OrderMapper orderMapper;
    private final YapePaymentDetailRepository yapePaymentDetailRepository;

    @Override
    public AdminDashboardSummaryResponse getSummary() {
        BigDecimal ventasValidadas = paymentRepository.sumValidatedPayments();

        return new AdminDashboardSummaryResponse(
                userRepository.count(),
                productRepository.count(),
                categoryRepository.count(),
                orderRepository.count(),
                orderRepository.countByEstado(OrderStatus.PENDIENTE_PAGO),
                paymentRepository.countByEstado(PaymentStatus.EN_REVISION),
                ventasValidadas
        );
    }

    @Override
    public List<OrderResponse> getRecentOrders() {
        return orderRepository.findTop5ByOrderByIdDesc()
                .stream()
                .map(order -> {
                    List<OrderDetail> details =
                            orderDetailRepository.findByOrderIdOrderByIdAsc(order.getId());

                    Payment payment = paymentRepository.findByOrderId(order.getId())
                            .orElse(null);

                    return orderMapper.toResponse(order, details, payment);
                })
                .toList();
    }

    @Override
    public List<AdminPaymentResponse> getRecentPayments() {
        return paymentRepository.findTop5ByOrderByIdDesc()
                .stream()
                .map(payment -> {
                    YapePaymentDetail detail =
                            yapePaymentDetailRepository.findByPaymentId(payment.getId())
                                    .orElse(null);

                    return new AdminPaymentResponse(
                            payment.getId(),
                            payment.getOrder().getId(),
                            payment.getOrder().getCodigoPedido(),
                            payment.getMetodo().name(),
                            payment.getEstado().name(),
                            payment.getMonto(),
                            detail != null ? detail.getNumeroOperacion() : null,
                            detail != null ? detail.getComprobanteUrl() : null,
                            detail != null ? detail.getObservacionAdmin() : null,
                            payment.getCreadoEn(),
                            payment.getValidadoEn()
                    );
                })
                .toList();
    }
}