package com.ayni.backend.user.service;

import com.ayni.backend.user.dto.AdminUserResponse;
import com.ayni.backend.user.dto.CreateAdminUserRequest;

import java.util.List;

public interface AdminUserService {

    List<AdminUserResponse> findAll();

    AdminUserResponse findById(Long userId);

    AdminUserResponse createAdmin(CreateAdminUserRequest request);

    AdminUserResponse toggleStatus(Long userId);
}