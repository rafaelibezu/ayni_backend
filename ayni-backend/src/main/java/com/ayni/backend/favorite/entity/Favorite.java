package com.ayni.backend.favorite.entity;

import com.ayni.backend.product.entity.Product;
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
@Table(
        name = "favoritos",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uq_favoritos_usuario_producto",
                        columnNames = {"usuario_id", "producto_id"}
                )
        }
)
public class Favorite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "usuario_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "producto_id", nullable = false)
    private Product product;

    @Column(name = "creado_en", nullable = false)
    private LocalDateTime creadoEn;
}