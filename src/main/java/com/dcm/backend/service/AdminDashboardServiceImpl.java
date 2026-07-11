package com.dcm.backend.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dcm.backend.dto.request.VendorLotRequest;
import com.dcm.backend.dto.response.ApiResponse;
import com.dcm.backend.dto.response.GoldSilverSalesStatsResponse;
import com.dcm.backend.dto.response.RecentTransactionsForDashboardResponse;
import com.dcm.backend.dto.response.StatCardsResponse;
import com.dcm.backend.dto.response.TopPerformingEmployeesResponse;
import com.dcm.backend.entity.EmployeeDetails;
import com.dcm.backend.entity.Transactions;
import com.dcm.backend.entity.VendorLotsDetails;
import com.dcm.backend.exception.ResourceNotFoundException;
import com.dcm.backend.exception.TransactionsNotFound;
import com.dcm.backend.repository.EmployeeRepository;
import com.dcm.backend.repository.TransactionRepository;
import com.dcm.backend.repository.VendorLotsRepository;
import com.dcm.backend.util.EmployeeSummaryProjection;

import lombok.RequiredArgsConstructor;
@Service
@RequiredArgsConstructor
@Transactional
public class AdminDashboardServiceImpl implements AdminDashboardService {

	private final TransactionRepository transactionRespository;
	private final EmployeeRepository employeeRepository; 
	private final VendorLotsRepository vendorLotsRepository;
	@Override
	public ApiResponse<StatCardsResponse> getCardsDetails(LocalDate date) throws TransactionsNotFound {
		
        LocalDateTime startOfToday = date.atStartOfDay(); 
        LocalDateTime endOfToday = date.atTime(LocalTime.MAX);
        if(!transactionRespository.existsByDay(startOfToday, endOfToday))
        {
        	throw new TransactionsNotFound("No Transactions found on that Day.");
        }
        LocalDateTime startOfYesterday = date.minusDays(1).atStartOfDay(); 
        LocalDateTime endOfYesterday = date.minusDays(1).atTime(LocalTime.MAX);
        Double totalSales=transactionRespository.getTotalSales(startOfToday, endOfToday);
        Double totalProfits=transactionRespository.getTotalProfits(startOfToday,endOfToday);
        Double totalGoldSold=transactionRespository.getTotalMetalSold(startOfToday, endOfToday,"GOLD");
        Double totalSilverSold=transactionRespository.getTotalMetalSold(startOfToday,endOfToday,"SILVER");
        Double totalYesterdaySales;
        Double totalYesterdayProfits;
        Double totalYesterdayGoldSold;
        Double totalYesterdaySilverSold;
        if(!transactionRespository.existsByDay(startOfYesterday, endOfYesterday))
        {
        	totalYesterdaySales=0.0;
        	totalYesterdayProfits=0.0;
        	totalYesterdayGoldSold=0.0;
        	totalYesterdaySilverSold=0.0;
        }
        else {
        	totalYesterdaySales=transactionRespository.getTotalSales(startOfYesterday, endOfYesterday);
            totalYesterdayProfits=transactionRespository.getTotalProfits(startOfYesterday,endOfYesterday);
            totalYesterdayGoldSold=transactionRespository.getTotalMetalSold(startOfYesterday, endOfYesterday,"GOLD");
            totalYesterdaySilverSold=transactionRespository.getTotalMetalSold(startOfYesterday,endOfYesterday,"SILVER");
        }
        Double salesProgress=(totalYesterdaySales==0?100.0:((totalSales-totalYesterdaySales)/totalYesterdaySales)*100);
        Double profitProgress=(totalYesterdayProfits==0?100.0:((totalProfits-totalYesterdayProfits)/totalYesterdayProfits)*100);
        Double goldProgress=(totalYesterdayGoldSold==0?100.0:((totalGoldSold-totalYesterdayGoldSold)/totalYesterdayGoldSold)*100);
        Double silverProgress=(totalYesterdaySilverSold==0?100.0:((totalSilverSold-totalYesterdaySilverSold)/totalYesterdaySilverSold)*100);
        
        StatCardsResponse statsResponse=StatCardsResponse.builder()
        		.totalSales(totalSales+"")
        		.totalProfits(totalProfits+"")
        		.totalOrders(transactionRespository.getTotalOrders(startOfToday, endOfToday)+"")
        		.totalGoldSold(totalGoldSold+"")
        		.totalSilverSold(totalSilverSold+"")
        		.totalEmployeesPresent(transactionRespository.getTotalEmployeesActive(startOfToday, endOfToday)+"")
        		.totalSalesProgress(salesProgress+"")
        		.totalProfitsProgress(profitProgress+"")
        		.totalGoldProgress(goldProgress+"")
        		.totalSilverProgress(silverProgress+"")
        		.build();
        return ApiResponse.<StatCardsResponse>builder()
        		.success(true)
        		.message("Stats fetched successfully.")
        		.data(statsResponse)
        		.timestamp(LocalDateTime.now())
        		.build();
	}
	@Override
	public ApiResponse<GoldSilverSalesStatsResponse> getGoldSilverStats(LocalDate date) throws TransactionsNotFound {
		LocalDateTime startOfToday = date.atStartOfDay(); 
        LocalDateTime endOfToday = date.atTime(LocalTime.MAX);
        if(!transactionRespository.existsByDay(startOfToday, endOfToday))
        {
        	throw new TransactionsNotFound("No Transactions found on that Day.");
        }
        Double goldSales=transactionRespository.getTotalMetalSales(startOfToday, endOfToday,"GOLD");
        Double silverSales=transactionRespository.getTotalMetalSales(startOfToday, endOfToday,"SILVER");
        Double totalSales=goldSales+silverSales;
        Double goldPercentage=(goldSales/totalSales)*100;
        Double silverPercentage=(silverSales/totalSales)*100;
        GoldSilverSalesStatsResponse goldSilverStats=GoldSilverSalesStatsResponse.builder()
        		.totalGoldSales(goldSales)
        		.totalSilverSales(silverSales)
        		.goldPercentage((int)Math.round(goldPercentage))
        		.silverPercentage((int)Math.round(silverPercentage))
        		.build();
        return ApiResponse.<GoldSilverSalesStatsResponse>builder()
        		.success(true)
        		.message("Successfully fetched stats!.")
        		.data(goldSilverStats)
        		.timestamp(LocalDateTime.now())
        		.build();
	}
	@Override
	public ApiResponse<List<RecentTransactionsForDashboardResponse>> getRecentTranasctions(LocalDate date) throws TransactionsNotFound,ResourceNotFoundException {
		LocalDateTime startOfToday = date.atStartOfDay(); 
        LocalDateTime endOfToday = date.atTime(LocalTime.MAX);
		List<Transactions> recentTransactions=this.transactionRespository.getRecentTransactions(startOfToday, endOfToday);
		List<RecentTransactionsForDashboardResponse> result=new ArrayList<>();
		if(recentTransactions.size() == 0)
			throw new TransactionsNotFound("No Transactions found to return.");
		else
		{
			for(Transactions t:recentTransactions) {
				Optional<EmployeeDetails> empDetails=this.employeeRepository.findByEmployeeId(t.getEmployeeId());
				EmployeeDetails employee= empDetails.orElseThrow(()-> new ResourceNotFoundException("Employee Details for given Employee id is not found"));
				result.add(
						RecentTransactionsForDashboardResponse.builder()
						.dateTime(t.getCreatedAt())
						.employeeName(employee.getEmployeeName())
						.totalGrams(t.getTotalGrams())
						.totalSaleAmount(t.getTotalAmount())
						.totalProfit(t.getProfit())
						.itemType(t.getItemType())
						.build()
						);
			}
			return ApiResponse.<List<RecentTransactionsForDashboardResponse>>builder()
					.success(true)
					.message("Fetched Recent Transactions Successfully!.")
					.data(result)
					.timestamp(LocalDateTime.now())
					.build();
		}
	}
	@Override
	public ApiResponse<List<TopPerformingEmployeesResponse>> getTopPerformanceEmployees(LocalDate date) {
		LocalDateTime startOfToday = date.atStartOfDay(); 
        LocalDateTime endOfToday = date.atTime(LocalTime.MAX);
		List<EmployeeSummaryProjection> listEmployees=this.transactionRespository.getEmployeeTransactionSummary(startOfToday, endOfToday);
		List<TopPerformingEmployeesResponse> result=new ArrayList<>();
		int i=1;
		for(EmployeeSummaryProjection e:listEmployees)
		{
			Optional<EmployeeDetails> empDetails=this.employeeRepository.findByEmployeeId(e.getEmployeeId());
			EmployeeDetails employee= empDetails.orElseThrow(()-> new ResourceNotFoundException("Employee Details for given Employee id is not found"));
			TopPerformingEmployeesResponse t=TopPerformingEmployeesResponse.builder()
					.rank(i++)
					.employeeName(employee.getEmployeeName())
					.sales(e.getTotalAmount()+"")
					.bills(e.getTransactionCount()+"")
					.build();
			result.add(t);
		}
		return ApiResponse.<List<TopPerformingEmployeesResponse>>builder()
				.success(true)
				.message("Here is the top performing Employees")
				.data(result)
				.timestamp(LocalDateTime.now())
				.build();
	}
	@Override
	public ApiResponse<Void> createNewVendorLot(VendorLotRequest request) {
		VendorLotsDetails vendor=VendorLotsDetails.builder()
				.vendorName(request.vendorName())
				.metalType(request.metalType())
				.productName(request.productName())
				.peices(request.peices())
				.grossWeight(request.grossWeight())
				.netWeight(request.netWeight())
				.fineWeight(request.fine())
				.stoneAmount(request.stoneAmount())
				.timeForPayment(request.timeForPayment())
				.build();
		Long id=this.vendorLotsRepository.save(vendor).getId();
		return ApiResponse.<Void>builder()
				.success(true)
				.message("Lot Created with id: "+id)
				.data(null)
				.timestamp(LocalDateTime.now())
				.build();
	}
	

}
