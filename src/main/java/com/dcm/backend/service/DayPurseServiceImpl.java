package com.dcm.backend.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dcm.backend.dto.response.ApiResponse;
import com.dcm.backend.dto.response.DayPurseResponse;
import com.dcm.backend.entity.DayPurse;
import com.dcm.backend.repository.DayPurseRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class DayPurseServiceImpl implements DayPurseService {

	private final DayPurseRepository dayPurseRepository;
	@Override
	public ApiResponse<Void> addMoneyToPurse(Long amount) {
		LocalDate today = LocalDate.now();
        LocalDateTime startOfToday = today.atStartOfDay(); 
        LocalDateTime endOfToday = today.atTime(LocalTime.MAX); 
		Optional<DayPurse> fetchedPurse=this.dayPurseRepository.findTopByCreatedAtBetweenOrderByIdDesc(startOfToday, endOfToday);
		Long currentAmount=0L;
		if(fetchedPurse.isEmpty())
		{
			currentAmount=amount;
		}
		else {
			currentAmount=fetchedPurse.get().getCurrentPurse()+amount;
		}
		DayPurse purse=DayPurse.builder()
				.amountAdded(amount)
				.currentPurse(currentAmount)
				.build();
		this.dayPurseRepository.save(purse);
		return ApiResponse.<Void>builder()
				.message("Amount Added to Purse")
				.success(true)
				.data(null)
				.timestamp(LocalDateTime.now())
				.build();
	}

	@Override
	public ApiResponse<List<DayPurseResponse>> fetchDayPurseDetails() {
		LocalDate today = LocalDate.now();
        LocalDateTime startOfToday = today.atStartOfDay(); 
        LocalDateTime endOfToday = today.atTime(LocalTime.MAX); 
		List<DayPurse> fetchedList=this.dayPurseRepository.findTop10ByCreatedAtBetween(startOfToday, endOfToday);
		List<DayPurseResponse> result=new ArrayList<>();
		for(DayPurse d:fetchedList)
		{
			DayPurseResponse dpr=DayPurseResponse.builder()
					.time(d.getCreatedAt())
					.amountAdded(d.getAmountAdded())
					.currentPurse(d.getCurrentPurse())
					.build();
			result.add(dpr);
		}
		return ApiResponse.<List<DayPurseResponse>>builder()
				.success(true)
				.message("Data fetched successfully")
				.data(result)
				.timestamp(LocalDateTime.now())
				.build();
	}

	@Override
	public ApiResponse<Map<String, Long>> fetchCurrentPurse() {
		LocalDate today = LocalDate.now();
        LocalDateTime startOfToday = today.atStartOfDay(); 
        LocalDateTime endOfToday = today.atTime(LocalTime.MAX); 
		Optional<DayPurse> fetchedData=this.dayPurseRepository.findTopByCreatedAtBetweenOrderByIdDesc(startOfToday, endOfToday);
		Map<String,Long> result=new HashMap<>();
		if(fetchedData.isEmpty())
		{
			result.put("currentPurse", 0L);
		}
		else
			result.put("currentPurse", fetchedData.get().getCurrentPurse());
		return ApiResponse.<Map<String,Long>>builder()
				.success(true)
				.message("Current Purse value.")
				.data(result)
				.timestamp(LocalDateTime.now())
				.build();
	}
	
}
