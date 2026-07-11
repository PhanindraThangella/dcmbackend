package com.dcm.backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record TransactionUpdateRequest(
		@NotNull(message = "Transaction ID is required")
		@Positive(message = "Transaction ID must be greater than zero")
		Long transactionId,
		@NotBlank(message = "Total Weightage is required.")
		String totalGrams,
		@NotBlank(message = "Rate PerGram is required.")
		String ratePerGram,
		@NotBlank(message = "Making Charges is required.")
		String makingCharges,
		@NotBlank(message = "Wastage is required.")
		String wastage,
		@NotBlank(message = "tagNumber is required.")
		String tagNumber,
		String stoneAmount
		) {

}
