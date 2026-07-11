package com.dcm.backend.dto.request;

public record UpdateTransactionRequest(
		Long tagNumber,
		Double totalAmount
		) {

}
