package com.dcm.backend.service;

import com.dcm.backend.dto.request.LoginRequest;
import com.dcm.backend.dto.request.RegisterRequest;
import com.dcm.backend.dto.response.ApiResponse;
import com.dcm.backend.dto.response.LoginResponse;

/**
 * Service responsible for authentication and employee registration.
 */
public interface AuthService {

    /**
     * Registers a new employee.
     *
     * @param request registration request
     * @return API response
     */
    ApiResponse<Void> register(RegisterRequest request);

    /**
     * Authenticates a user and generates JWT.
     *
     * @param request login request
     * @return login response containing JWT
     */
    ApiResponse<LoginResponse> login(LoginRequest request);

}