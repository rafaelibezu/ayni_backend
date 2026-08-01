package com.ayni.backend.order.service;

import com.ayni.backend.cart.entity.Cart;
import com.ayni.backend.cart.entity.CartItem;
import com.ayni.backend.cart.repository.CartItemRepository;
import com.ayni.backend.cart.repository.CartRepository;
import com.ayni.backend.common.constants.MessageError;
import com.ayni.backend.common.enums.OrderStatus;
import com.ayni.backend.common.enums.PaymentMethod;
import com.ayni.backend.common.enums.PaymentStatus;
import com.ayni.backend.common.exception.BadRequestException;
import com.ayni.backend.common.exception.ResourceNotFoundException;
import com.ayni.backend.common.util.GlobalUtil;
import com.ayni.backend.order.dto.CheckoutRequest;
import com.ayni.backend.order.dto.OrderResponse;
import com.ayni.backend.order.dto.RegisterYapePaymentRequest;
import com.ayni.backend.order.entity.Address;
import com.ayni.backend.order.entity.Order;
import com.ayni.backend.order.entity.OrderDetail;
import com.ayni.backend.order.mapper.OrderMapper;
import com.ayni.backend.order.repository.AddressRepository;
import com.ayni.backend.order.repository.OrderDetailRepository;
import com.ayni.backend.order.repository.OrderRepository;
import com.ayni.backend.payment.entity.Payment;
import com.ayni.backend.payment.entity.YapePaymentDetail;
import com.ayni.backend.payment.repository.PaymentRepository;
import com.ayni.backend.payment.repository.YapePaymentDetailRepository;
import com.ayni.backend.product.entity.Product;
import com.ayni.backend.product.repository.ProductRepository;
import com.ayni.backend.user.entity.User;
import com.ayni.backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ayni.backend.common.util.SecurityUtil;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import com.ayni.backend.notification.service.EmailService;
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final EmailService emailService;
    private static final BigDecimal DEFAULT_SHIPPING_COST = new BigDecimal("5.00");

    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final AddressRepository addressRepository;
    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final PaymentRepository paymentRepository;
    private final YapePaymentDetailRepository yapePaymentDetailRepository;
    private final OrderMapper orderMapper;

    @Override
    @Transactional
    public OrderResponse checkout(CheckoutRequest request) {
        User user = userRepository.findByEmail(SecurityUtil.getCurrentUserEmail())
                .orElseThrow(() -> new ResourceNotFoundException(MessageError.USER_NOT_FOUND));

        Cart cart = cartRepository.findByUserEmailAndActivoTrue(SecurityUtil.getCurrentUserEmail())
                .orElseThrow(() -> new BadRequestException(MessageError.CART_EMPTY));

        List<CartItem> cartItems = cartItemRepository.findByCartIdOrderByIdAsc(cart.getId());

        if (cartItems.isEmpty()) {
            throw new BadRequestException(MessageError.CART_EMPTY);
        }

        validateStock(cartItems);

        Address address = resolveCheckoutAddress(user, request);

        BigDecimal subtotal = calculateSubtotal(cartItems);
        BigDecimal total = subtotal.add(DEFAULT_SHIPPING_COST);

        Order order = Order.builder()
                .user(user)
                .address(address)
                .codigoPedido(generateOrderCode())
                .estado(OrderStatus.PENDIENTE_PAGO)
                .subtotal(subtotal)
                .costoEnvio(DEFAULT_SHIPPING_COST)
                .total(total)
                .observaciones(request.observaciones())
                .creadoEn(LocalDateTime.now())
                .build();

        Order savedOrder = orderRepository.save(order);

        List<OrderDetail> details = saveOrderDetails(savedOrder, cartItems);
        emailService.sendOrderCreated(
                user.getEmail(),
                savedOrder.getCodigoPedido(),
                savedOrder.getTotal().toString()
        );
        discountStock(cartItems);

        cartItemRepository.deleteByCartId(cart.getId());
        cart.setActualizadoEn(LocalDateTime.now());

        return orderMapper.toResponse(savedOrder, details, null);
    }

    @Override
    @Transactional
    public OrderResponse registerYapePayment(Long orderId, RegisterYapePaymentRequest request) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido no encontrado."));

        if (!order.getUser().getEmail().equals(SecurityUtil.getCurrentUserEmail())) {
            throw new ResourceNotFoundException("Pedido no encontrado.");
        }

        if (paymentRepository.findByOrderId(orderId).isPresent()) {
            throw new BadRequestException("Este pedido ya tiene un pago registrado.");
        }

        Payment payment = Payment.builder()
                .order(order)
                .metodo(PaymentMethod.YAPE)
                .estado(PaymentStatus.EN_REVISION)
                .monto(order.getTotal())
                .referenciaExterna(null)
                .creadoEn(LocalDateTime.now())
                .build();

        Payment savedPayment = paymentRepository.save(payment);

        YapePaymentDetail yapeDetail = YapePaymentDetail.builder()
                .payment(savedPayment)
                .numeroOperacion(request.numeroOperacion())
                .comprobanteUrl(request.comprobanteUrl())
                .observacionAdmin("Pago Yape pendiente de validación.")
                .creadoEn(LocalDateTime.now())
                .build();

        yapePaymentDetailRepository.save(yapeDetail);

        order.setEstado(OrderStatus.PAGO_EN_REVISION);
        order.setActualizadoEn(LocalDateTime.now());

        Order updatedOrder = orderRepository.save(order);

        List<OrderDetail> details = orderDetailRepository.findByOrderIdOrderByIdAsc(updatedOrder.getId());
        emailService.sendYapePaymentReceived(
                updatedOrder.getUser().getEmail(),
                updatedOrder.getCodigoPedido()
        );
        return orderMapper.toResponse(updatedOrder, details, savedPayment);
    }

    @Override
    public List<OrderResponse> findMyOrders() {
        return orderRepository.findByUserEmailOrderByIdDesc(SecurityUtil.getCurrentUserEmail())
                .stream()
                .map(order -> {
                    List<OrderDetail> details = orderDetailRepository.findByOrderIdOrderByIdAsc(order.getId());
                    Payment payment = paymentRepository.findByOrderId(order.getId()).orElse(null);

                    return orderMapper.toResponse(order, details, payment);
                })
                .toList();
    }

    private Address resolveCheckoutAddress(User user, CheckoutRequest request) {
        if (request.addressId() != null) {
            return addressRepository.findByIdAndUserEmail(
                            request.addressId(),
                            user.getEmail()
                    )
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Dirección no encontrada o no pertenece al usuario."
                    ));
        }

        validateManualAddress(request);

        Address address = Address.builder()
                .user(user)
                .destinatario(GlobalUtil.cleanText(request.destinatario()))
                .celular(GlobalUtil.cleanText(request.celular()))
                .documentoTipo(GlobalUtil.cleanText(request.documentoTipo()))
                .documentoNumero(GlobalUtil.cleanText(request.documentoNumero()))
                .direccion(GlobalUtil.cleanText(request.direccion()))
                .distrito(GlobalUtil.cleanText(request.distrito()))
                .referencia(GlobalUtil.cleanText(request.referencia()))
                .esPrincipal(false)
                .creadoEn(LocalDateTime.now())
                .build();

        return addressRepository.save(address);
    }

    private void validateManualAddress(CheckoutRequest request) {
        if (GlobalUtil.isBlank(request.destinatario())) {
            throw new BadRequestException("El destinatario es obligatorio.");
        }

        if (GlobalUtil.isBlank(request.celular())) {
            throw new BadRequestException("El celular es obligatorio.");
        }

        if (GlobalUtil.isBlank(request.direccion())) {
            throw new BadRequestException("La dirección es obligatoria.");
        }

        if (GlobalUtil.isBlank(request.distrito())) {
            throw new BadRequestException("El distrito es obligatorio.");
        }
    }

    private void validateStock(List<CartItem> cartItems) {
        for (CartItem item : cartItems) {
            Product product = item.getProduct();

            if (!Boolean.TRUE.equals(product.getActivo())) {
                throw new BadRequestException(MessageError.PRODUCT_NOT_FOUND);
            }

            if (item.getCantidad() > product.getStock()) {
                throw new BadRequestException(MessageError.STOCK_NOT_AVAILABLE);
            }
        }
    }

    private BigDecimal calculateSubtotal(List<CartItem> cartItems) {
        return cartItems.stream()
                .map(item -> item.getPrecioUnitario()
                        .multiply(BigDecimal.valueOf(item.getCantidad())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private List<OrderDetail> saveOrderDetails(Order order, List<CartItem> cartItems) {
        List<OrderDetail> details = cartItems.stream()
                .map(item -> OrderDetail.builder()
                        .order(order)
                        .product(item.getProduct())
                        .nombreProducto(item.getProduct().getNombre())
                        .sku(item.getProduct().getSku())
                        .cantidad(item.getCantidad())
                        .precioUnitario(item.getPrecioUnitario())
                        .subtotal(item.getPrecioUnitario()
                                .multiply(BigDecimal.valueOf(item.getCantidad())))
                        .creadoEn(LocalDateTime.now())
                        .build())
                .toList();

        return orderDetailRepository.saveAll(details);
    }

    private String generateOrderCode() {
        String timestamp = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));

        return "AYNI-" + timestamp;
    }

    private void discountStock(List<CartItem> cartItems) {
        for (CartItem item : cartItems) {
            Product product = productRepository.findById(item.getProduct().getId())
                    .orElseThrow(() -> new ResourceNotFoundException(MessageError.PRODUCT_NOT_FOUND));

            int newStock = product.getStock() - item.getCantidad();

            if (newStock < 0) {
                throw new BadRequestException(MessageError.STOCK_NOT_AVAILABLE);
            }

            product.setStock(newStock);
            product.setActualizadoEn(LocalDateTime.now());

            productRepository.save(product);
        }
    }

    @Override
    public OrderResponse findMyOrderById(Long orderId) {
        String email = SecurityUtil.getCurrentUserEmail();

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException(MessageError.ORDER_NOT_FOUND));

        if (!order.getUser().getEmail().equals(email)) {
            throw new BadRequestException("No puedes acceder a este pedido.");
        }

        List<OrderDetail> details =
                orderDetailRepository.findByOrderIdOrderByIdAsc(order.getId());

        Payment payment = paymentRepository.findByOrderId(order.getId())
                .orElse(null);

        return orderMapper.toResponse(order, details, payment);
    }

    @Override
    @Transactional
    public OrderResponse cancelMyOrder(Long orderId) {
        String email = SecurityUtil.getCurrentUserEmail();

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException(MessageError.ORDER_NOT_FOUND));

        if (!order.getUser().getEmail().equals(email)) {
            throw new BadRequestException("No puedes cancelar este pedido.");
        }

        if (order.getEstado() != OrderStatus.PENDIENTE_PAGO
                && order.getEstado() != OrderStatus.PAGO_EN_REVISION) {
            throw new BadRequestException(
                    "Solo puedes cancelar pedidos pendientes o en revisión de pago."
            );
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


}