package com.ayni.backend.order.repository;

import com.ayni.backend.order.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AddressRepository extends JpaRepository<Address, Long> {

    Optional<Address> findByUserEmailAndEsPrincipalTrue(String email);

    List<Address> findByUserEmailOrderByEsPrincipalDescIdDesc(String email);

    Optional<Address> findByIdAndUserEmail(Long id, String email);
}