package com.dcm.backend.dto.response;

import java.time.LocalDateTime;

import com.dcm.backend.enums.ItemType;

import lombok.Builder;
@Builder
public record TransactionResponse(
		Long transactionId,
		LocalDateTime dateTime,
		Double totalGrams,
		Long ratePerGram,
		Double totalAmount,
		ItemType itemType
) {
}
