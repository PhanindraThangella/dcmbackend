package com.dcm.backend.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dcm.backend.dto.request.LoginRequest;
import com.dcm.backend.dto.request.RegisterRequest;
import com.dcm.backend.dto.response.ApiResponse;
import com.dcm.backend.dto.response.LoginResponse;
import com.dcm.backend.entity.Credentials;
import com.dcm.backend.entity.EmployeeDetails;
import com.dcm.backend.enums.Role;
import com.dcm.backend.enums.Status;
import com.dcm.backend.exception.AuthenticationFailedException;
import com.dcm.backend.exception.ResourceAlreadyExistsException;
import com.dcm.backend.repository.CredentialsRepository;
import com.dcm.backend.repository.EmployeeRepository;
import com.dcm.backend.security.CustomUserDetails;
import com.dcm.backend.security.JwtService;
import com.dcm.backend.util.EmployeeIdGenerator;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthServiceImpl implements AuthService {

    private final EmployeeRepository employeeRepository;

    private final CredentialsRepository credentialsRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;

    private final EmployeeIdGenerator employeeIdGenerator;

    @Override
    public ApiResponse<Void> register(RegisterRequest request) {

        if (employeeRepository.existsByEmployeePhoneNumber(
               Long.parseLong( request.employeePhoneNumber()))) {

        	throw new ResourceAlreadyExistsException("Phone number already registered.");
        }

        if (employeeRepository.existsByEmployeeEmail(
                request.employeeEmail())) {

        	throw new ResourceAlreadyExistsException("Email already registered.");
        }

        if (credentialsRepository.existsByUsername(
                request.employeePhoneNumber())) {

        	throw new ResourceAlreadyExistsException("Username already exists.");
        }

        EmployeeDetails employee = EmployeeDetails.builder()
                .employeeId(employeeIdGenerator.generateEmployeeId())
                .employeeName(request.employeeName())
                .nickname(request.nickname())
                .employeeDOB(request.employeeDOB())
                .employeePhoneNumber(Long.parseLong(request.employeePhoneNumber()))
                .employeeAddress(request.employeeAddress())
                .employeeEmail(request.employeeEmail())
                .build();

        employeeRepository.save(employee);

        Credentials credentials = Credentials.builder()
                .username(request.employeePhoneNumber())
                .password(passwordEncoder.encode(request.password()))
                .role(Role.TAGGENERATOR)
                .status(Status.ACTIVE)
                .employee(employee)
                .build();

        credentialsRepository.save(credentials);

        employee.setCredentials(credentials);

        return ApiResponse.<Void>builder()
                .success(true)
                .message("Employee registered successfully.")
                .timestamp(java.time.LocalDateTime.now())
                .build();
    }

    @Override
    public ApiResponse<LoginResponse> login(LoginRequest request) {

        try {

            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.username(),
                            request.password()));

        } catch (BadCredentialsException ex) {
            throw new AuthenticationFailedException("Invalid username or password.");
        }

        Credentials credentials = credentialsRepository
                .findByUsername(request.username())
                .orElseThrow(() ->
                        new AuthenticationFailedException("Invalid username or password."));

        CustomUserDetails user = new CustomUserDetails(credentials);

        String token = jwtService.generateToken(user);

        LoginResponse loginResponse = LoginResponse.builder()
                .accessToken(token)
                .tokenType("Bearer")
                .employeeId(user.getEmployeeId())
                .employeeName(user.getEmployeeName())
                .nickname(user.getNickname())
                .username(user.getUsername())
                .role(credentials.getRole())
                .build();

        return ApiResponse.<LoginResponse>builder()
                .success(true)
                .message("Login successful.")
                .data(loginResponse)
                .timestamp(java.time.LocalDateTime.now())
                .build();
    }

	@Override
	public ApiResponse<List<String>> getEmployeesNames() {
		List<String> response=this.employeeRepository.findAllEmployeeNames();
		return ApiResponse.<List<String>>builder()
				.success(true)
				.message("Employees Names fetched successfully.")
				.data(response)
				.timestamp(LocalDateTime.now())
				.build();
	}

}