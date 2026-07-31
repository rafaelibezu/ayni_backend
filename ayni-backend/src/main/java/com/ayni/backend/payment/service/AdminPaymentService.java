package com.ayni.backend.payment.service;

import com.ayni.backend.payment.dto.AdminPaymentResponse;
import com.ayni.backend.payment.dto.UpdatePaymentStatusRequest;

import java.util.List;

public interface AdminPaymentService {

    List<AdminPaymentResponse> findAll();

    AdminPaymentResponse updateStatus(
            Long paymentId,
            UpdatePaymentStatusRequest request
    );
}