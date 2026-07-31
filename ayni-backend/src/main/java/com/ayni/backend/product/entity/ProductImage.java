package com.ayni.backend.product.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "producto_imagenes")
public class ProductImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_id", nullable = false)
    private Product product;

    @Column(name = "imagen_url", nullable = false, length = 500)
    private String imagenUrl;

    @Column(name = "orden", nullable = false)
    private Integer orden;

    @Column(name = "creado_en", nullable = false)
    private LocalDateTime creadoEn;
}