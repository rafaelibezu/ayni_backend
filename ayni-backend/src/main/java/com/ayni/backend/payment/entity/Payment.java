package com.ayni.backend.payment.entity;

import com.ayni.backend.common.enums.PaymentMethod;
import com.ayni.backend.common.enums.PaymentStatus;
import com.ayni.backend.order.entity.Order;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "pagos")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "pedido_id", nullable = false)
    private Order order;

    @Enumerated(EnumType.STRING)
    @Column(name = "metodo", nullable = false, length = 30)
    private PaymentMethod metodo;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 30)
    private PaymentStatus estado;

    @Column(name = "monto", nullable = false, precision = 10, scale = 2)
    private BigDecimal monto;

    @Column(name = "referencia_externa", length = 150)
    private String referenciaExterna;

    @Column(name = "creado_en", nullable = false)
    private LocalDateTime creadoEn;

    @Column(name = "actualizado_en")
    private LocalDateTime actualizadoEn;

    @Column(name = "validado_en")
    private LocalDateTime validadoEn;
}