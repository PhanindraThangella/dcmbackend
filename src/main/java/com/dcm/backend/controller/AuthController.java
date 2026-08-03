package com.dcm.backend.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dcm.backend.dto.request.LoginRequest;
import com.dcm.backend.dto.request.RegisterRequest;
import com.dcm.backend.dto.response.ApiResponse;
import com.dcm.backend.dto.response.LoginResponse;
import com.dcm.backend.service.AuthService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * REST controller for authentication operations.
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Validated
public class AuthController {

    private final AuthService authService;

    /**
     * Register a new employee.
     *
     * @param request registration request
     * @return registration response
     */
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Void>> register(
            @Valid @RequestBody RegisterRequest request) {

        ApiResponse<Void> response = authService.register(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);
    }

    /**
     * Authenticate employee.
     *
     * @param request login request
     * @return JWT response
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(
            @Valid @RequestBody LoginRequest request) {

        return ResponseEntity.ok(authService.login(request));
    }
    @GetMapping("/getEmployeeNames")
    public ResponseEntity<ApiResponse<List<String>>> getEmployeeNames()
    {
    	ApiResponse<List<String>> response=this.authService.getEmployeesNames();
    	return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}