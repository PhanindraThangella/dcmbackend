package com.dcm.backend.dto.response;

import lombok.Builder;

@Builder
public record GoldSilverSalesStatsResponse(
		Double totalGoldSales,
		Double totalSilverSales,
		int goldPercentage,
		int silverPercentage
		) {

}
