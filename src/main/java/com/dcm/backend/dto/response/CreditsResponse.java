package com.dcm.backend.dto.response;

import lombok.Builder;

@Builder
public record CreditsResponse(
		String customerName,
		Long contactNumber,
		Long totalAmount,
		Long id
) {}
