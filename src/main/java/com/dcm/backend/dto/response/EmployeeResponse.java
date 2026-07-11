package com.dcm.backend.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Builder;

/**
 * Employee response DTO.
 *
 * @param employeeId Employee business ID.
 * @param employeeName Employee name.
 * @param nickname Employee nickname.
 * @param employeeDOB Employee DOB.
 * @param employeePhoneNumber Employee phone number.
 * @param employeeAddress Employee address.
 * @param employeeEmail Employee email.
 * @param createdAt Account creation timestamp.
 */
@Builder
public record EmployeeResponse(

        String employeeId,

        String employeeName,

        String nickname,

        LocalDate employeeDOB,

        Long employeePhoneNumber,

        String employeeAddress,

        String employeeEmail,

        LocalDateTime createdAt
) {
}