package com.ayni.backend.user.service;

import com.ayni.backend.common.constants.MessageError;
import com.ayni.backend.common.exception.ResourceNotFoundException;
import com.ayni.backend.common.util.SecurityUtil;
import com.ayni.backend.order.entity.Order;
import com.ayni.backend.order.repository.OrderRepository;
import com.ayni.backend.user.dto.UserLastOrderResponse;
import com.ayni.backend.user.dto.UserProfileResponse;
import com.ayni.backend.user.entity.User;
import com.ayni.backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.ayni.backend.common.util.GlobalUtil;
import com.ayni.backend.user.dto.UpdateUserProfileRequest;
import java.time.LocalDateTime;

import com.ayni.backend.common.exception.BadRequestException;
import com.ayni.backend.user.dto.ChangePasswordRequest;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserProfileResponse getMyProfile() {
        String email = SecurityUtil.getCurrentUserEmail();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException(MessageError.USER_NOT_FOUND));

        return buildProfileResponse(user);
    }

    private UserLastOrderResponse toLastOrderResponse(Order order) {
        return new UserLastOrderResponse(
                order.getId(),
                order.getCodigoPedido(),
                order.getEstado().name(),
                order.getTotal(),
                order.getCreadoEn()
        );
    }

    @Override
    public UserProfileResponse updateMyProfile(UpdateUserProfileRequest request) {
        String email = SecurityUtil.getCurrentUserEmail();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException(MessageError.USER_NOT_FOUND));

        user.setNombres(GlobalUtil.cleanText(request.nombres()));
        user.setApellidos(GlobalUtil.cleanText(request.apellidos()));
        user.setCelular(GlobalUtil.cleanText(request.celular()));
        user.setDocumentoTipo(GlobalUtil.cleanText(request.documentoTipo()));
        user.setDocumentoNumero(GlobalUtil.cleanText(request.documentoNumero()));
        user.setActualizadoEn(LocalDateTime.now());

        User savedUser = userRepository.save(user);

        return buildProfileResponse(savedUser);
    }

    private UserProfileResponse buildProfileResponse(User user) {
        String email = user.getEmail();

        UserLastOrderResponse lastOrder = orderRepository.findTopByUserEmailOrderByIdDesc(email)
                .map(this::toLastOrderResponse)
                .orElse(null);

        return new UserProfileResponse(
                user.getId(),
                user.getNombres(),
                user.getApellidos(),
                user.getEmail(),
                user.getCelular(),
                user.getDocumentoTipo(),
                user.getDocumentoNumero(),
                user.getActivo(),
                user.getEmailVerificado(),
                user.getRoles()
                        .stream()
                        .map(role -> role.getNombre().name())
                        .toList(),
                lastOrder,
                user.getCreadoEn()
        );
    }

    @Override
    public void changePassword(ChangePasswordRequest request) {
        String email = SecurityUtil.getCurrentUserEmail();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException(MessageError.USER_NOT_FOUND));

        if (!passwordEncoder.matches(request.currentPassword(), user.getPasswordHash())) {
            throw new BadRequestException("La contraseña actual es incorrecta.");
        }

        if (passwordEncoder.matches(request.newPassword(), user.getPasswordHash())) {
            throw new BadRequestException("La nueva contraseña no puede ser igual a la actual.");
        }

        user.setPasswordHash(passwordEncoder.encode(request.newPassword()));
        user.setActualizadoEn(LocalDateTime.now());

        userRepository.save(user);
    }
}