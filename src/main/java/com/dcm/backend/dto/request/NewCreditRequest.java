package com.dcm.backend.dto.request;

public record NewCreditRequest(
		String customerName,
		Long mobileNumber,
		Long creditAmount
		) {
}
