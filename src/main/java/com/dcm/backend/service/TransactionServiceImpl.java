	package com.dcm.backend.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dcm.backend.dto.request.TransactionRegRequest;
import com.dcm.backend.dto.request.TransactionUpdateRequest;
import com.dcm.backend.dto.response.ApiResponse;
import com.dcm.backend.dto.response.TransactionResponse;
import com.dcm.backend.entity.Transactions;
import com.dcm.backend.enums.ItemType;
import com.dcm.backend.enums.TransactionStatus;
import com.dcm.backend.exception.TransactionsNotFound;
import com.dcm.backend.repository.TransactionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class TransactionServiceImpl implements TransactionService{

	private final TransactionRepository transactionRepository;
	@Override
	public ApiResponse<Void> addTransaction(TransactionRegRequest transactionRequest) {
		Transactions transaction=this.transactionRepository.findFirstByOrderByIdDesc();
		if(transaction!=null)
		{
			if(transaction.getEmployeeId().equals(transactionRequest.employeeId())
					&& transaction.getTotalGrams().equals(transactionRequest.totalGrams())
					&& transaction.getRatePerGram().equals(transactionRequest.ratePerGram())
					&& transaction.getWastage().equals(transactionRequest.wastage())
					&& transaction.getMakingCharges().equals(transactionRequest.makingCharges())
					&& transaction.getTagNumber().equals(transactionRequest.tagNumber())) {
			return ApiResponse.<Void>builder()
				.success(false)
				.message("Duplicate Transaction found")
				.timestamp(LocalDateTime.now())
				.data(null)
				.build();
			}
			else
			{
				double profit=Double.parseDouble(transactionRequest.wastage())*Integer.parseInt(transactionRequest.ratePerGram())+Integer.parseInt(transactionRequest.makingCharges());
				double totalAmount=Double.parseDouble(transactionRequest.totalGrams())*Integer.parseInt(transactionRequest.ratePerGram())+profit;
				ItemType itemType=(Integer.parseInt(transactionRequest.ratePerGram())>1000?ItemType.GOLD:ItemType.SILVER);
				Transactions newTransaction=Transactions.builder()
				.employeeId(transactionRequest.employeeId())
				.totalGrams(Double.parseDouble(transactionRequest.totalGrams()))
				.ratePerGram(Long.parseLong(transactionRequest.ratePerGram()))
				.makingCharges(Long.parseLong(transactionRequest.makingCharges()))
				.wastage(Double.parseDouble(transactionRequest.wastage()))
				.tagNumber(Long.parseLong(transactionRequest.tagNumber()))
				.totalAmount(totalAmount)
				.profit(profit)
				.itemType(itemType)
				.transactionStatus(TransactionStatus.PENDING)
				.stoneAmount(transactionRequest.stoneAmount()!=null?Long.parseLong(transactionRequest.stoneAmount()):0)
				.build();
				transactionRepository.save(newTransaction);
				return ApiResponse.<Void>builder()
						.success(true)
						.message("Transaction Added Successfully.")
						.data(null)
						.timestamp(LocalDateTime.now())
						.build();
			}
		}
		else
		{
			double profit=Double.parseDouble(transactionRequest.wastage())*Integer.parseInt(transactionRequest.ratePerGram())+Integer.parseInt(transactionRequest.makingCharges());
			double totalAmount=Double.parseDouble(transactionRequest.totalGrams())*Integer.parseInt(transactionRequest.ratePerGram())+profit;
			ItemType itemType=(Integer.parseInt(transactionRequest.ratePerGram())>1000?ItemType.GOLD:ItemType.SILVER);
			Transactions newTransaction=Transactions.builder()
			.employeeId(transactionRequest.employeeId())
			.totalGrams(Double.parseDouble(transactionRequest.totalGrams()))
			.ratePerGram(Long.parseLong(transactionRequest.ratePerGram()))
			.makingCharges(Long.parseLong(transactionRequest.makingCharges()))
			.wastage(Double.parseDouble(transactionRequest.wastage()))
			.tagNumber(Long.parseLong(transactionRequest.tagNumber()))
			.totalAmount(totalAmount)
			.profit(profit)
			.itemType(itemType)
			.transactionStatus(TransactionStatus.PENDING)
			.stoneAmount(transactionRequest.stoneAmount()!=null?Long.parseLong(transactionRequest.stoneAmount()):0)
			.build();
			transactionRepository.save(newTransaction);
			return ApiResponse.<Void>builder()
					.success(true)
					.message("Transaction Added Successfully.")
					.data(null)
					.timestamp(LocalDateTime.now())
					.build();
		}
	}
	@Override
	public ApiResponse<List<TransactionResponse>> getAllTransactionsOnDate(String employeeId) throws TransactionsNotFound{
		LocalDate today = LocalDate.now();
        LocalDateTime startOfToday = today.atStartOfDay(); 
        LocalDateTime endOfToday = today.atTime(LocalTime.MAX); 
		List<Transactions> transactions=transactionRepository.findByEmployeeIdAndCreatedAtBetween(employeeId, startOfToday, endOfToday);
		if(transactions.size()==0)
		{
			throw new TransactionsNotFound("No Transactions found.");
		}
		List<TransactionResponse> result=new ArrayList<TransactionResponse>();
		for(Transactions t:transactions) {
			TransactionResponse tr=TransactionResponse.builder()
					.transactionId(t.getId())
					.dateTime(t.getCreatedAt())
					.itemType(t.getItemType())
					.totalGrams(t.getTotalGrams())
					.ratePerGram(t.getRatePerGram())
					.totalAmount(t.getTotalAmount())
					.build();
			result.add(tr);
		}
		return ApiResponse.<List<TransactionResponse>>builder()
				.success(true)
				.message("Total transactions today")
				.data(result)
				.timestamp(LocalDateTime.now())
				.build();
	}
	@Override
	public ApiResponse<Void> updateTransaction(TransactionUpdateRequest transactionUpdateRequest) throws TransactionsNotFound {
		Optional<Transactions> existingRecord=this.transactionRepository.findById(transactionUpdateRequest.transactionId());
		Transactions existingTransation= existingRecord.orElseThrow(()->new TransactionsNotFound("No transaction found to update"));
		existingTransation.setTotalGrams(Double.parseDouble(transactionUpdateRequest.totalGrams()));
		existingTransation.setRatePerGram(Long.parseLong(transactionUpdateRequest.ratePerGram()));
		existingTransation.setMakingCharges(Long.parseLong(transactionUpdateRequest.makingCharges()));
		existingTransation.setWastage(Double.parseDouble(transactionUpdateRequest.wastage()));
		existingTransation.setTagNumber(Long.parseLong(transactionUpdateRequest.tagNumber()));
		ItemType itemType=(Integer.parseInt(transactionUpdateRequest.ratePerGram())>300?ItemType.GOLD:ItemType.SILVER);
		existingTransation.setItemType(itemType);
		this.transactionRepository.save(existingTransation);
		return ApiResponse.<Void>builder()
				.success(true)
				.message("Transaction Updated Successfully!.")
				.data(null)
				.timestamp(LocalDateTime.now())
				.build();
	}
	

}
