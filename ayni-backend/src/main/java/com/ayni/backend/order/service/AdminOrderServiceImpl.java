package com.ayni.backend.order.service;

import com.ayni.backend.common.constants.MessageError;
import com.ayni.backend.common.enums.OrderStatus;
import com.ayni.backend.common.exception.BadRequestException;
import com.ayni.backend.common.exception.ResourceNotFoundException;
import com.ayni.backend.order.dto.OrderResponse;
import com.ayni.backend.order.dto.UpdateOrderStatusRequest;
import com.ayni.backend.order.entity.Order;
import com.ayni.backend.order.entity.OrderDetail;
import com.ayni.backend.order.mapper.OrderMapper;
import com.ayni.backend.order.repository.OrderDetailRepository;
import com.ayni.backend.order.repository.OrderRepository;
import com.ayni.backend.payment.entity.Payment;
import com.ayni.backend.payment.repository.PaymentRepository;
import com.ayni.backend.product.entity.Product;
import com.ayni.backend.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminOrderServiceImpl implements AdminOrderService {

    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final PaymentRepository paymentRepository;
    private final OrderMapper orderMapper;
    private final ProductRepository productRepository;

    @Override
    @Transactional
    public OrderResponse updateStatus(Long orderId, UpdateOrderStatusRequest request) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException(MessageError.ORDER_NOT_FOUND));

        validateTransition(order.getEstado(), request.estado());

        order.setEstado(request.estado());
        order.setActualizadoEn(LocalDateTime.now());

        Order savedOrder = orderRepository.save(order);

        List<OrderDetail> details =
                orderDetailRepository.findByOrderIdOrderByIdAsc(savedOrder.getId());

        Payment payment = paymentRepository.findByOrderId(savedOrder.getId())
                .orElse(null);

        return orderMapper.toResponse(savedOrder, details, payment);
    }

    @Override
    public List<OrderResponse> findAll() {
        return orderRepository.findAllByOrderByIdDesc()
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
    public OrderResponse findById(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException(MessageError.ORDER_NOT_FOUND));

        List<OrderDetail> details =
                orderDetailRepository.findByOrderIdOrderByIdAsc(order.getId());

        Payment payment = paymentRepository.findByOrderId(order.getId())
                .orElse(null);

        return orderMapper.toResponse(order, details, payment);
    }

    @Override
    @Transactional
    public OrderResponse cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException(MessageError.ORDER_NOT_FOUND));

        if (order.getEstado() == OrderStatus.CANCELADO) {
            throw new BadRequestException("El pedido ya está cancelado.");
        }

        if (order.getEstado() == OrderStatus.ENTREGADO) {
            throw new BadRequestException("No se puede cancelar un pedido entregado.");
        }

        List<OrderDetail> details =
                orderDetailRepository.findByOrderIdOrderByIdAsc(order.getId());

        restoreStock(details);

        order.setEstado(OrderStatus.CANCELADO);
        order.setActualizadoEn(LocalDateTime.now());

        Order savedOrder = orderRepository.save(order);

        Payment payment = paymentRepository.findByOrderId(savedOrder.getId())
                .orElse(null);

        return orderMapper.toResponse(savedOrder, details, payment);
    }

    private void restoreStock(List<OrderDetail> details) {
        for (OrderDetail detail : details) {
            Product product = detail.getProduct();

            product.setStock(product.getStock() + detail.getCantidad());
            product.setActualizadoEn(LocalDateTime.now());

            productRepository.save(product);
        }
    }

    private void validateTransition(OrderStatus current, OrderStatus next) {
        if (current == OrderStatus.CANCELADO || current == OrderStatus.ENTREGADO) {
            throw new BadRequestException(
                    "No se puede modificar un pedido finalizado o cancelado."
            );
        }

        if (next == OrderStatus.PAGADO && current != OrderStatus.PAGO_EN_REVISION) {
            throw new BadRequestException(
                    "El pedido solo puede marcarse como PAGADO desde PAGO_EN_REVISION."
            );
        }

        if (next == OrderStatus.PREPARANDO && current != OrderStatus.PAGADO) {
            throw new BadRequestException(
                    "El pedido solo puede pasar a PREPARANDO después de PAGADO."
            );
        }

        if (next == OrderStatus.ENVIADO && current != OrderStatus.PREPARANDO) {
            throw new BadRequestException(
                    "El pedido solo puede pasar a ENVIADO desde PREPARANDO."
            );
        }

        if (next == OrderStatus.ENTREGADO && current != OrderStatus.ENVIADO) {
            throw new BadRequestException(
                    "El pedido solo puede pasar a ENTREGADO desde ENVIADO."
            );
        }
    }
}