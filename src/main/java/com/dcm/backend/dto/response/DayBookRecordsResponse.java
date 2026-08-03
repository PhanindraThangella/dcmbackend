package com.dcm.backend.dto.response;

import lombok.Builder;

@Builder
public record DayBookRecordsResponse(
		String typeOfPayment,
		String itemType,
		Long tagNumber,
		Double totalGrams,
		Long totalAmount,
		Long cashPaid,
		Long upiPaid,
		Double oldGoldGrams,
		Long oldGoldAmount,
		Double oldSilverGrams,
		Long oldSilverAmount,
		Long creditAmount,
		String customerName,
		Long mobileNumber,
		String employeeName
		) {

}
