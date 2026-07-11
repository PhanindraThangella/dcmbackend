package com.dcm.backend.dto.request;

import java.time.LocalDate;

public record VendorLotRequest(
		String vendorName,
		String metalType,
		String productName,
		Long peices,
		Double grossWeight,
		Double netWeight,
		Double fine,
		Long stoneAmount,
		LocalDate timeForPayment
		
		) {

}
