package com.dcm.backend.dto.response;

import com.dcm.backend.enums.Role;

import lombok.Builder;

/**
 * Response returned after successful authentication.
 */
@Builder
public record LoginResponse(

        String accessToken,

        String tokenType,

        Long expiresIn,

        String employeeId,

        String employeeName,

        String nickname,

        String username,

        Role role
) {
}