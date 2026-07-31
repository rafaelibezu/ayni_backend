package com.ayni.backend.auth.service;

import com.ayni.backend.auth.dto.AuthResponse;
import com.ayni.backend.auth.dto.LoginRequest;
import com.ayni.backend.auth.dto.RegisterRequest;

public interface AuthService {

    AuthResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);
}