package com.ayni.backend.product.entity;

import com.ayni.backend.category.entity.Category;
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
@Table(name = "productos")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_id", nullable = false)
    private Category category;

    @Column(name = "nombre", nullable = false, length = 150)
    private String nombre;

    @Column(name = "slug", nullable = false, unique = true, length = 180)
    private String slug;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "sku", nullable = false, unique = true, length = 80)
    private String sku;

    @Column(name = "precio", nullable = false, precision = 10, scale = 2)
    private BigDecimal precio;

    @Column(name = "precio_comparacion", precision = 10, scale = 2)
    private BigDecimal precioComparacion;

    @Column(name = "stock", nullable = false)
    private Integer stock;

    @Column(name = "imagen_principal_url", length = 500)
    private String imagenPrincipalUrl;

    @Column(name = "badge", length = 50)
    private String badge;

    @Column(name = "destacado", nullable = false)
    private Boolean destacado;

    @Column(name = "activo", nullable = false)
    private Boolean activo;

    @Column(name = "creado_en", nullable = false)
    private LocalDateTime creadoEn;

    @Column(name = "actualizado_en")
    private LocalDateTime actualizadoEn;
}