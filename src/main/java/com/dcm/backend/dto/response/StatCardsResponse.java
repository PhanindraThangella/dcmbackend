package com.dcm.backend.dto.response;

import lombok.Builder;

@Builder
public record StatCardsResponse(
		String totalSales,
		String totalProfits,
		String totalGoldSold,
		String totalSilverSold,
		String totalEmployeesPresent,
		String totalSalesProgress,
		String totalProfitsProgress,
		String totalGoldProgress,
		String totalSilverProgress,
		String totalOrders
		) {
}
