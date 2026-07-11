package com.dcm.backend.dto.response;

import java.time.LocalDateTime;

import com.dcm.backend.enums.ItemType;

import lombok.Builder;

@Builder
public record RecentTransactionsForDashboardResponse(
		LocalDateTime dateTime,
		String employeeName,
		Double totalSaleAmount,
		Double totalProfit,
		Double totalGrams,
		ItemType itemType
		) {

}
