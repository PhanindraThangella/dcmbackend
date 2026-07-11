package com.dcm.backend.dto.response;

import lombok.Builder;

@Builder
public record TopPerformingEmployeesResponse(
		int rank,
		String employeeName,
		String sales,
		String bills
		) {}
