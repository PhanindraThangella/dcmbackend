package com.dcm.backend.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dcm.backend.dto.request.VendorLotRequest;
import com.dcm.backend.dto.response.ApiResponse;
import com.dcm.backend.dto.response.GoldSilverSalesStatsResponse;
import com.dcm.backend.dto.response.RecentTransactionsForDashboardResponse;
import com.dcm.backend.dto.response.StatCardsResponse;
import com.dcm.backend.dto.response.TopPerformingEmployeesResponse;
import com.dcm.backend.exception.ResourceNotFoundException;
import com.dcm.backend.exception.TransactionsNotFound;
import com.dcm.backend.service.AdminDashboardService;

import lombok.RequiredArgsConstructor;
@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminServiceController {
	private final AdminDashboardService adminService;
	
	@GetMapping("/getStatsForDashboard/{date}")
	public ResponseEntity<ApiResponse<StatCardsResponse>> getStats(@PathVariable("date") String date) throws TransactionsNotFound
	{
		LocalDate newDate=LocalDate.parse(date);
		ApiResponse<StatCardsResponse> response=adminService.getCardsDetails(newDate);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
	@GetMapping("/getGoldSilverStats/{date}")
	public ResponseEntity<ApiResponse<GoldSilverSalesStatsResponse>> getGoldSilverStats(@PathVariable("date") String date) throws TransactionsNotFound
	{
		LocalDate newDate=LocalDate.parse(date);
		ApiResponse<GoldSilverSalesStatsResponse> response=adminService.getGoldSilverStats(newDate);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
	@GetMapping("/getRecentTransaction/{date}")
	public ResponseEntity<ApiResponse<List<RecentTransactionsForDashboardResponse>>> getRecentTransactionsForDashboard(@PathVariable("date") String date) throws TransactionsNotFound,ResourceNotFoundException 
	{
		LocalDate newDate=LocalDate.parse(date);
		ApiResponse<List<RecentTransactionsForDashboardResponse>> response=adminService.getRecentTranasctions(newDate);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
	@GetMapping("/getTopPerformingEmployees/{date}")
	public ResponseEntity<ApiResponse<List<TopPerformingEmployeesResponse>>> getTopPerformingEmployees(@PathVariable("date") String date) throws TransactionsNotFound,ResourceNotFoundException 
	{
		LocalDate newDate=LocalDate.parse(date);
		ApiResponse<List<TopPerformingEmployeesResponse>> response=adminService.getTopPerformanceEmployees(newDate);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
	@PostMapping("/createNewLot")
	public ResponseEntity<ApiResponse<Void>> createNewLot(@RequestBody VendorLotRequest request)
	{
		ApiResponse<Void> response=this.adminService.createNewVendorLot(request);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
}
