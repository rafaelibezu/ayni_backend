package com.ayni.backend.payment.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "pagos_yape_detalle")
public class YapePaymentDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "pago_id", nullable = false)
    private Payment payment;

    @Column(name = "numero_operacion", nullable = false, length = 100)
    private String numeroOperacion;

    @Column(name = "comprobante_url", length = 500)
    private String comprobanteUrl;

    @Column(name = "observacion_admin", length = 500)
    private String observacionAdmin;

    @Column(name = "creado_en", nullable = false)
    private LocalDateTime creadoEn;
}