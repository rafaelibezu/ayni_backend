package com.ayni.backend.payment.repository;

import com.ayni.backend.common.enums.PaymentStatus;
import com.ayni.backend.payment.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Optional<Payment> findByOrderId(Long orderId);

    List<Payment> findAllByOrderByIdDesc();

    List<Payment> findTop5ByOrderByIdDesc();

    long countByEstado(PaymentStatus estado);

    @Query("SELECT COALESCE(SUM(p.monto), 0) FROM Payment p WHERE p.estado = com.ayni.backend.common.enums.PaymentStatus.VALIDADO")
    BigDecimal sumValidatedPayments();
}