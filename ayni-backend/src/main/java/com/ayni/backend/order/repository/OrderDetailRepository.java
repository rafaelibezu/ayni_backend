package com.ayni.backend.order.repository;

import com.ayni.backend.order.entity.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {

    List<OrderDetail> findByOrderIdOrderByIdAsc(Long orderId);
}