package com.ayni.backend.user.controller;

import com.ayni.backend.common.constants.ApiRoutes;
import com.ayni.backend.common.response.ApiResponse;
import com.ayni.backend.user.dto.AdminUserResponse;
import com.ayni.backend.user.dto.CreateAdminUserRequest;
import com.ayni.backend.user.service.AdminUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class AdminUserController {

    private final AdminUserService adminUserService;

    @GetMapping(ApiRoutes.ADMIN + "/users")
    public ApiResponse<List<AdminUserResponse>> findAll() {
        return ApiResponse.ok(
                "Usuarios obtenidos correctamente.",
                adminUserService.findAll()
        );
    }

    @GetMapping(ApiRoutes.ADMIN + "/users/{userId}")
    public ApiResponse<AdminUserResponse> findById(
            @PathVariable Long userId
    ) {
        return ApiResponse.ok(
                "Usuario obtenido correctamente.",
                adminUserService.findById(userId)
        );
    }

    @PostMapping(ApiRoutes.ADMIN + "/users/admin")
    public ApiResponse<AdminUserResponse> createAdmin(
            @Valid @RequestBody CreateAdminUserRequest request
    ) {
        return ApiResponse.ok(
                "Usuario administrador creado correctamente.",
                adminUserService.createAdmin(request)
        );
    }

    @PatchMapping(ApiRoutes.ADMIN + "/users/{userId}/toggle-status")
    public ApiResponse<AdminUserResponse> toggleStatus(
            @PathVariable Long userId
    ) {
        return ApiResponse.ok(
                "Estado del usuario actualizado correctamente.",
                adminUserService.toggleStatus(userId)
        );
    }
}