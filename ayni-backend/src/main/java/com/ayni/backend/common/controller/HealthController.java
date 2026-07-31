package com.ayni.backend.common.controller;

import com.ayni.backend.common.constants.ApiRoutes;
import com.ayni.backend.common.response.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    @GetMapping(ApiRoutes.HEALTH)
    public ApiResponse<String> health() {
        return ApiResponse.ok(
                "Backend activo.",
                "Ayni backend funcionando correctamente"
        );
    }
}