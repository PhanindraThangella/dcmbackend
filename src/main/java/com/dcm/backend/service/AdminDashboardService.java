package com.dcm.backend.service;

import java.time.LocalDate;
import java.util.List;

import com.dcm.backend.dto.request.VendorLotRequest;
import com.dcm.backend.dto.response.ApiResponse;
import com.dcm.backend.dto.response.GoldSilverSalesStatsResponse;
import com.dcm.backend.dto.response.RecentTransactionsForDashboardResponse;
import com.dcm.backend.dto.response.StatCardsResponse;
import com.dcm.backend.dto.response.TopPerformingEmployeesResponse;
import com.dcm.backend.exception.ResourceNotFoundException;
import com.dcm.backend.exception.TransactionsNotFound;

public interface AdminDashboardService {
	ApiResponse<StatCardsResponse> getCardsDetails(LocalDate date) throws TransactionsNotFound;
	ApiResponse<GoldSilverSalesStatsResponse> getGoldSilverStats(LocalDate date) throws TransactionsNotFound;
	ApiResponse<List<RecentTransactionsForDashboardResponse>> getRecentTranasctions(LocalDate date) throws TransactionsNotFound,ResourceNotFoundException ;
	ApiResponse<List<TopPerformingEmployeesResponse>> getTopPerformanceEmployees(LocalDate date);
	ApiResponse<Void> createNewVendorLot(VendorLotRequest request);
}
