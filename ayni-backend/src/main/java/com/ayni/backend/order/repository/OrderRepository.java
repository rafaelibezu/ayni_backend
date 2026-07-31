package com.ayni.backend.order.repository;

import com.ayni.backend.common.enums.OrderStatus;
import com.ayni.backend.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByUserEmailOrderByIdDesc(String email);

    Optional<Order> findTopByUserEmailOrderByIdDesc(String email);

    List<Order> findAllByOrderByIdDesc();

    List<Order> findTop5ByOrderByIdDesc();

    long countByEstado(OrderStatus estado);
}