package com.dcm.backend.dto.request;

import java.time.LocalDate;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;

/**
 * Request DTO used for employee registration.
 */
@Builder
public record RegisterRequest(

        @NotBlank(message = "Employee name is required.")
        String employeeName,

        @NotBlank(message = "Nickname is required.")
        String nickname,

        @NotNull(message = "Date of birth is required.")
        LocalDate employeeDOB,

        @NotBlank(message = "Phone number is required.")
        @Pattern(
                regexp = "^[6-9]\\d{9}$",
                message = "Please enter a valid 10-digit Indian mobile number."
        )
        String employeePhoneNumber,

        @NotBlank(message = "Address is required.")
        String employeeAddress,

        @NotBlank(message = "Email is required.")
        @Email(message = "Please enter a valid email address.")
        String employeeEmail,

        @NotBlank(message = "Password is required.")
        @Pattern(
                regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,20}$",
                message = """
                        Password must be 8-20 characters long and contain at least
                        one uppercase letter, one lowercase letter,
                        one digit and one special character.
                        """
        )
        String password
) {
}