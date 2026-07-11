package com.dcm.backend.dto.request;

public record TagGenerateRequest(
		Long lotNo,
		String metalType,
		String productName,
		Long peices,
		Double grossWeight,
		Double netWeight,
		Double stoneWeight,
		Long stoneAmount
		) {
}
