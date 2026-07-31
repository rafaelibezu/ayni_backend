package com.ayni.backend.order.entity;

import com.ayni.backend.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "direcciones")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "usuario_id", nullable = false)
    private User user;

    @Column(name = "destinatario", nullable = false, length = 150)
    private String destinatario;

    @Column(name = "celular", nullable = false, length = 20)
    private String celular;

    @Column(name = "documento_tipo", length = 20)
    private String documentoTipo;

    @Column(name = "documento_numero", length = 20)
    private String documentoNumero;

    @Column(name = "direccion", nullable = false, length = 255)
    private String direccion;

    @Column(name = "distrito", nullable = false, length = 100)
    private String distrito;

    @Column(name = "referencia", length = 255)
    private String referencia;

    @Column(name = "es_principal", nullable = false)
    private Boolean esPrincipal;

    @Column(name = "creado_en", nullable = false)
    private LocalDateTime creadoEn;

    @Column(name = "actualizado_en")
    private LocalDateTime actualizadoEn;
}