package com.ayni.backend.auth.service;

import com.ayni.backend.auth.dto.AuthResponse;
import com.ayni.backend.auth.dto.LoginRequest;
import com.ayni.backend.auth.dto.RegisterRequest;
import com.ayni.backend.common.constants.AppConstants;
import com.ayni.backend.common.constants.MessageError;
import com.ayni.backend.common.enums.AuthProvider;
import com.ayni.backend.common.enums.RoleName;
import com.ayni.backend.common.exception.BusinessException;
import com.ayni.backend.common.exception.ResourceNotFoundException;
import com.ayni.backend.common.util.GlobalUtil;
import com.ayni.backend.role.entity.Role;
import com.ayni.backend.role.repository.RoleRepository;
import com.ayni.backend.user.entity.User;
import com.ayni.backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.ayni.backend.security.jwt.JwtService;
import com.ayni.backend.security.userdetail.CustomUserDetails;

import java.time.LocalDateTime;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Override
    public AuthResponse register(RegisterRequest request) {
        String email = GlobalUtil.normalizeEmail(request.email());

        if (userRepository.existsByEmail(email)) {
            throw new BusinessException(MessageError.EMAIL_ALREADY_EXISTS);
        }

        Role userRole = roleRepository.findByNombre(RoleName.USER)
                .orElseThrow(() -> new ResourceNotFoundException(MessageError.ROLE_NOT_FOUND));

        User user = User.builder()
                .nombres(GlobalUtil.cleanText(request.nombres()))
                .apellidos(GlobalUtil.cleanText(request.apellidos()))
                .email(email)
                .passwordHash(passwordEncoder.encode(request.password()))
                .proveedorAuth(AuthProvider.LOCAL)
                .googleId(null)
                .celular(GlobalUtil.cleanText(request.celular()))
                .documentoTipo(null)
                .documentoNumero(null)
                .activo(true)
                .emailVerificado(false)
                .creadoEn(LocalDateTime.now())
                .roles(Set.of(userRole))
                .build();

        User savedUser = userRepository.save(user);

        String token = jwtService.generateToken(new CustomUserDetails(savedUser));
        return toAuthResponse(savedUser, token);
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        String email = GlobalUtil.normalizeEmail(request.email());

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException(MessageError.INVALID_CREDENTIALS));

        if (!Boolean.TRUE.equals(user.getActivo())) {
            throw new BusinessException(MessageError.INVALID_CREDENTIALS);
        }

        if (user.getPasswordHash() == null ||
                !passwordEncoder.matches(request.password(), user.getPasswordHash())) {
            throw new BusinessException(MessageError.INVALID_CREDENTIALS);
        }

        String token = jwtService.generateToken(new CustomUserDetails(user));
        return toAuthResponse(user, token);
    }

    private AuthResponse toAuthResponse(User user, String token) {
        return new AuthResponse(
                user.getId(),
                user.getNombres(),
                user.getApellidos(),
                user.getEmail(),
                token,
                user.getRoles()
                        .stream()
                        .map(role -> role.getNombre().name())
                        .toList()
        );
    }
}