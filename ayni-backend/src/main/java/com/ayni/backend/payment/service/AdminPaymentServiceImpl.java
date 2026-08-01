package com.ayni.backend.payment.service;

import com.ayni.backend.common.enums.OrderStatus;
import com.ayni.backend.common.enums.PaymentStatus;
import com.ayni.backend.common.exception.BadRequestException;
import com.ayni.backend.common.exception.ResourceNotFoundException;
import com.ayni.backend.order.entity.Order;
import com.ayni.backend.order.repository.OrderRepository;
import com.ayni.backend.payment.dto.AdminPaymentResponse;
import com.ayni.backend.payment.dto.UpdatePaymentStatusRequest;
import com.ayni.backend.payment.entity.Payment;
import com.ayni.backend.payment.entity.YapePaymentDetail;
import com.ayni.backend.payment.repository.PaymentRepository;
import com.ayni.backend.payment.repository.YapePaymentDetailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.time.LocalDateTime;
import com.ayni.backend.notification.service.EmailService;

@Service
@RequiredArgsConstructor
public class AdminPaymentServiceImpl implements AdminPaymentService {

    private final PaymentRepository paymentRepository;
    private final YapePaymentDetailRepository yapePaymentDetailRepository;
    private final OrderRepository orderRepository;
    private final EmailService emailService;

    @Override
    @Transactional
    public AdminPaymentResponse updateStatus(
            Long paymentId,
            UpdatePaymentStatusRequest request
    ) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new ResourceNotFoundException("Pago no encontrado."));

        if (payment.getEstado() == PaymentStatus.VALIDADO) {
            throw new BadRequestException("El pago ya fue validado.");
        }

        if (payment.getEstado() == PaymentStatus.RECHAZADO) {
            throw new BadRequestException("El pago ya fue rechazado.");
        }

        payment.setEstado(request.estado());
        payment.setActualizadoEn(LocalDateTime.now());

        if (request.estado() == PaymentStatus.VALIDADO) {
            payment.setValidadoEn(LocalDateTime.now());
        }

        Payment savedPayment = paymentRepository.save(payment);

        YapePaymentDetail yapeDetail =
                yapePaymentDetailRepository.findByPaymentId(paymentId)
                        .orElse(null);

        if (yapeDetail != null) {
            yapeDetail.setObservacionAdmin(request.observacion());
            yapePaymentDetailRepository.save(yapeDetail);
        }

        updateOrderStatus(savedPayment.getOrder(), request.estado());

        if (savedPayment.getEstado() == PaymentStatus.VALIDADO) {
            emailService.sendPaymentValidated(
                    savedPayment.getOrder().getUser().getEmail(),
                    savedPayment.getOrder().getCodigoPedido()
            );
        }

        return toResponse(savedPayment, yapeDetail);
    }

    private void updateOrderStatus(Order order, PaymentStatus paymentStatus) {
        if (paymentStatus == PaymentStatus.VALIDADO) {
            order.setEstado(OrderStatus.PAGADO);
        } else if (paymentStatus == PaymentStatus.RECHAZADO) {
            order.setEstado(OrderStatus.CANCELADO);
        }

        order.setActualizadoEn(LocalDateTime.now());
        orderRepository.save(order);
    }

    private AdminPaymentResponse toResponse(
            Payment payment,
            YapePaymentDetail detail
    ) {
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
    }

    @Override
    public List<AdminPaymentResponse> findAll() {
        return paymentRepository.findAllByOrderByIdDesc()
                .stream()
                .map(payment -> {
                    YapePaymentDetail detail =
                            yapePaymentDetailRepository.findByPaymentId(payment.getId())
                                    .orElse(null);

                    return toResponse(payment, detail);
                })
                .toList();
    }

}