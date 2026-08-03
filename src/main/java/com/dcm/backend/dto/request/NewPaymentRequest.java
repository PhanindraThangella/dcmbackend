package com.dcm.backend.dto.request;

public record NewPaymentRequest(
		String tagNumber,
		Long totalCash,
		Long totalUpi,
		Double ogGrams,
		Long ogAmount,
		String creditId,
		String itemType
		) {

}
