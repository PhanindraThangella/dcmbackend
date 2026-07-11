package com.dcm.backend.service;

import java.util.List;
import java.util.Map;

import com.dcm.backend.dto.response.ApiResponse;
import com.dcm.backend.dto.response.DayPurseResponse;

public interface DayPurseService {
	ApiResponse<Void> addMoneyToPurse(Long amount);
	ApiResponse<List<DayPurseResponse>> fetchDayPurseDetails();
	ApiResponse<Map<String,Long>> fetchCurrentPurse();
}
