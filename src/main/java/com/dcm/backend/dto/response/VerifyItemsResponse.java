package com.dcm.backend.dto.response;

import lombok.Builder;

@Builder
public record VerifyItemsResponse(
		Long tagNumber,
		Long peices,
		Double grossWeight,
		Double netWeight
		) {
}
