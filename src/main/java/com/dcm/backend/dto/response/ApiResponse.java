package com.dcm.backend.dto.response;

import java.time.LocalDateTime;

import lombok.Builder;

/**
 * Generic API response wrapper.
 *
 * @param <T> payload type
 */
@Builder
public record ApiResponse<T>(

        boolean success,

        String message,

        T data,

        LocalDateTime timestamp

) {
}