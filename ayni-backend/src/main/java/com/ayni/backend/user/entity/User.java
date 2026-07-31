package com.ayni.backend.user.entity;

import com.ayni.backend.common.enums.AuthProvider;
import com.ayni.backend.role.entity.Role;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "usuarios")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nombres", nullable = false, length = 100)
    private String nombres;

    @Column(name = "apellidos", length = 100)
    private String apellidos;

    @Column(name = "email", nullable = false, unique = true, length = 150)
    private String email;

    @Column(name = "password_hash", length = 255)
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    @Column(name = "proveedor_auth", nullable = false, length = 30)
    private AuthProvider proveedorAuth;

    @Column(name = "google_id", length = 255)
    private String googleId;

    @Column(name = "celular", length = 20)
    private String celular;

    @Column(name = "documento_tipo", length = 20)
    private String documentoTipo;

    @Column(name = "documento_numero", length = 20)
    private String documentoNumero;

    @Column(name = "activo", nullable = false)
    private Boolean activo;

    @Column(name = "email_verificado", nullable = false)
    private Boolean emailVerificado;

    @Column(name = "creado_en", nullable = false)
    private LocalDateTime creadoEn;

    @Column(name = "actualizado_en")
    private LocalDateTime actualizadoEn;

    @Builder.Default
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "usuario_roles",
            joinColumns = @JoinColumn(name = "usuario_id"),
            inverseJoinColumns = @JoinColumn(name = "rol_id")
    )
    private Set<Role> roles = new HashSet<>();
}