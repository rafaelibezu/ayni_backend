package com.ayni.backend.role.repository;

import com.ayni.backend.common.enums.RoleName;
import com.ayni.backend.role.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByNombre(RoleName nombre);
}