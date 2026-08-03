package com.dcm.backend.dto.request;

public record AddEmployeeSalaryRequest(
		String employeeName,
		Long amount
		) {

}
