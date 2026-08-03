package com.dcm.backend.dto.response;

import lombok.Builder;

@Builder
public record ItemDetailsResponse(
		  Double grossWeight,
		  Long peices,
		  Double netWeight,
		  Double stoneWeight,
		  Long stoneAmount,
		  String ProductName,
		  Long id,
		  Long tagNumber,
		  String itemType
		) {

}
