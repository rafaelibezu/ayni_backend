package com.ayni.backend.auth.controller;

import com.ayni.backend.auth.dto.AuthResponse;
import com.ayni.backend.auth.dto.LoginRequest;
import com.ayni.backend.auth.dto.RegisterRequest;
import com.ayni.backend.auth.service.AuthService;
import com.ayni.backend.common.constants.ApiRoutes;
import com.ayni.backend.common.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping(ApiRoutes.AUTH + "/register")
    public ApiResponse<AuthResponse> register(
            @Valid @RequestBody RegisterRequest request
    ) {
        return ApiResponse.ok(
                "Usuario registrado correctamente.",
                authService.register(request)
        );
    }

    @PostMapping(ApiRoutes.AUTH + "/login")
    public ApiResponse<AuthResponse> login(
            @Valid @RequestBody LoginRequest request
    ) {
        return ApiResponse.ok(
                "Inicio de sesión correcto.",
                authService.login(request)
        );
    }
}