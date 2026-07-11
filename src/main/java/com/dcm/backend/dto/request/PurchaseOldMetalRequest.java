package com.dcm.backend.dto.request;

public record PurchaseOldMetalRequest(
		String selectedMetal,
		Double weight,
		Long amount
		) {
}
