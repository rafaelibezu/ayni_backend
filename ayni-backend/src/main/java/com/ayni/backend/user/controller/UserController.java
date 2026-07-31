package com.ayni.backend.user.controller;

import com.ayni.backend.common.constants.ApiRoutes;
import com.ayni.backend.common.response.ApiResponse;
import com.ayni.backend.user.dto.UpdateUserProfileRequest;
import com.ayni.backend.user.dto.UserProfileResponse;
import com.ayni.backend.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import com.ayni.backend.user.dto.ChangePasswordRequest;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping(ApiRoutes.USERS + "/me")
    public ApiResponse<UserProfileResponse> getMyProfile() {
        return ApiResponse.ok(
                "Perfil obtenido correctamente.",
                userService.getMyProfile()
        );
    }

    @PutMapping(ApiRoutes.USERS + "/me")
    public ApiResponse<UserProfileResponse> updateMyProfile(
            @Valid @RequestBody UpdateUserProfileRequest request
    ) {
        return ApiResponse.ok(
                "Perfil actualizado correctamente.",
                userService.updateMyProfile(request)
        );
    }

    @PutMapping(ApiRoutes.USERS + "/me/password")
    public ApiResponse<Void> changePassword(
            @Valid @RequestBody ChangePasswordRequest request
    ) {
        userService.changePassword(request);

        return ApiResponse.ok(
                "Contraseña actualizada correctamente.",
                null
        );
    }
}