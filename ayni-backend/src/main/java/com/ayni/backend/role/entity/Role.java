package com.ayni.backend.role.entity;

import com.ayni.backend.common.enums.RoleName;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "nombre", nullable = false, unique = true, length = 50)
    private RoleName nombre;

    @Column(name = "descripcion", length = 150)
    private String descripcion;

    @Column(name = "creado_en", nullable = false)
    private LocalDateTime creadoEn;
}