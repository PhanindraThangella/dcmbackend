package com.dcm.backend.dto.response;

import com.dcm.backend.enums.ItemType;

import lombok.Builder;

@Builder
public record PendingTransactionsResponse(
		Long tagNumber,
		Double totalGrams,
		Double wastage,
		Long ratePerGram,
		Long makingCharges,
		Double totalAmount,
		ItemType itemType
		) {

}
