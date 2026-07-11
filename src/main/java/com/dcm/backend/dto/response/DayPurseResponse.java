package com.dcm.backend.dto.response;

import java.time.LocalDateTime;

import lombok.Builder;

@Builder
public record DayPurseResponse(
		LocalDateTime time,
		Long amountAdded,
		Long currentPurse	
		) {}
