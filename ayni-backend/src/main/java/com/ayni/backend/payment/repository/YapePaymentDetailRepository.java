package com.ayni.backend.payment.repository;

import com.ayni.backend.payment.entity.YapePaymentDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface YapePaymentDetailRepository extends JpaRepository<YapePaymentDetail, Long> {

    Optional<YapePaymentDetail> findByPaymentId(Long paymentId);
}