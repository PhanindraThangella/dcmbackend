package com.dcm.backend.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dcm.backend.dto.response.ApiResponse;
import com.dcm.backend.dto.response.DayPurseResponse;
import com.dcm.backend.service.DayPurseService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/purse")
@RequiredArgsConstructor
public class DayPurseController {
	private final DayPurseService dayPurseService;
	@PostMapping("/addMoneyToPurse/{amount}")
	public ResponseEntity<ApiResponse<Void>> addMoneyToPurse(@PathVariable("amount") Long amount)
	{
		ApiResponse<Void> response=this.dayPurseService.addMoneyToPurse(amount);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
	@GetMapping("/getPurseRecords")
	public ResponseEntity<ApiResponse<List<DayPurseResponse>>> fetchDayPurseRecords()
	{
		ApiResponse<List<DayPurseResponse>> response=this.dayPurseService.fetchDayPurseDetails();
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
	@GetMapping("/getCurrentPurse")
	public ResponseEntity<ApiResponse<Map<String,Long>>> fetchCurrentPurse()
	{
		ApiResponse<Map<String,Long>> response=this.dayPurseService.fetchCurrentPurse();
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
}
