package com.ayni.backend.user.service;

import com.ayni.backend.common.constants.MessageError;
import com.ayni.backend.common.enums.AuthProvider;
import com.ayni.backend.common.enums.RoleName;
import com.ayni.backend.common.exception.BadRequestException;
import com.ayni.backend.common.exception.ResourceNotFoundException;
import com.ayni.backend.common.util.GlobalUtil;
import com.ayni.backend.role.entity.Role;
import com.ayni.backend.role.repository.RoleRepository;
import com.ayni.backend.user.dto.AdminUserResponse;
import com.ayni.backend.user.dto.CreateAdminUserRequest;
import com.ayni.backend.user.entity.User;
import com.ayni.backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminUserServiceImpl implements AdminUserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public List<AdminUserResponse> findAll() {
        return userRepository.findAllByOrderByIdDesc()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public AdminUserResponse findById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(MessageError.USER_NOT_FOUND));

        return toResponse(user);
    }

    @Override
    @Transactional
    public AdminUserResponse createAdmin(CreateAdminUserRequest request) {
        String email = GlobalUtil.normalizeEmail(request.email());

        if (userRepository.existsByEmail(email)) {
            throw new BadRequestException(MessageError.EMAIL_ALREADY_EXISTS);
        }

        Role adminRole = roleRepository.findByNombre(RoleName.ADMIN)
                .orElseThrow(() -> new ResourceNotFoundException(MessageError.ROLE_NOT_FOUND));

        Set<Role> roles = new HashSet<>();
        roles.add(adminRole);

        User user = User.builder()
                .nombres(GlobalUtil.cleanText(request.nombres()))
                .apellidos(GlobalUtil.cleanText(request.apellidos()))
                .email(email)
                .passwordHash(passwordEncoder.encode(request.password()))
                .celular(GlobalUtil.cleanText(request.celular()))
                .documentoTipo(GlobalUtil.cleanText(request.documentoTipo()))
                .documentoNumero(GlobalUtil.cleanText(request.documentoNumero()))
                .proveedorAuth(AuthProvider.LOCAL)
                .activo(true)
                .emailVerificado(true)
                .roles(roles)
                .creadoEn(LocalDateTime.now())
                .build();

        return toResponse(userRepository.save(user));
    }

    @Override
    @Transactional
    public AdminUserResponse toggleStatus(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(MessageError.USER_NOT_FOUND));

        user.setActivo(!Boolean.TRUE.equals(user.getActivo()));
        user.setActualizadoEn(LocalDateTime.now());

        return toResponse(userRepository.save(user));
    }

    private AdminUserResponse toResponse(User user) {
        Set<String> roles = user.getRoles()
                .stream()
                .map(role -> role.getNombre().name())
                .collect(Collectors.toSet());

        return new AdminUserResponse(
                user.getId(),
                user.getNombres(),
                user.getApellidos(),
                user.getEmail(),
                user.getCelular(),
                user.getDocumentoTipo(),
                user.getDocumentoNumero(),
                user.getActivo(),
                user.getEmailVerificado(),
                user.getProveedorAuth().name(),
                roles,
                user.getCreadoEn()
        );
    }
}