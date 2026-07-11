package com.dcm.backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

/**
 * Request DTO for user authentication.
 *
 * @param username Username used for login.
 * @param password Plain text password.
 */
@Builder
public record LoginRequest(

        @NotBlank(message = "Username is required.")
        String username,

        @NotBlank(message = "Password is required.")
        String password
) {
}