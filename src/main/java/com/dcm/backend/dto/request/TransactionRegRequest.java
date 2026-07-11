package com.dcm.backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
@Builder
public record TransactionRegRequest(
		@NotBlank(message = "EmployeeID is required.")
		String employeeId,
		@NotBlank(message = "Total Grams is required.")
		String totalGrams,
		@NotBlank(message = "Rate Per Gram is required.")
		String ratePerGram,
		@NotBlank(message = "Making Charges is required.")
		String makingCharges,
		@NotBlank(message = "Wastage is required.")
		String wastage,
		@NotBlank(message = "Tag Number is required.")
		String tagNumber,
		String stoneAmount
) {
}
