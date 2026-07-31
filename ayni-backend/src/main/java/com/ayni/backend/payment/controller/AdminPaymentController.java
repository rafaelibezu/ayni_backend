package com.ayni.backend.payment.controller;

import com.ayni.backend.common.constants.ApiRoutes;
import com.ayni.backend.common.response.ApiResponse;
import com.ayni.backend.payment.dto.AdminPaymentResponse;
import com.ayni.backend.payment.dto.UpdatePaymentStatusRequest;
import com.ayni.backend.payment.service.AdminPaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class AdminPaymentController {

    private final AdminPaymentService adminPaymentService;

    @PutMapping(ApiRoutes.ADMIN + "/payments/{paymentId}/status")
    public ApiResponse<AdminPaymentResponse> updateStatus(
            @PathVariable Long paymentId,
            @Valid @RequestBody UpdatePaymentStatusRequest request
    ) {
        return ApiResponse.ok(
                "Estado del pago actualizado correctamente.",
                adminPaymentService.updateStatus(paymentId, request)
        );
    }

    @GetMapping(ApiRoutes.ADMIN + "/payments")
    public ApiResponse<List<AdminPaymentResponse>> findAll() {
        return ApiResponse.ok(
                "Pagos obtenidos correctamente.",
                adminPaymentService.findAll()
        );
    }
}