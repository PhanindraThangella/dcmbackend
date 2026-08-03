package com.dcm.backend.service;

import java.util.List;
import java.util.Map;

import com.dcm.backend.dto.request.AddEmployeeSalaryRequest;
import com.dcm.backend.dto.request.NewCreditRequest;
import com.dcm.backend.dto.request.NewPaymentRequest;
import com.dcm.backend.dto.request.PurchaseOldMetalRequest;
import com.dcm.backend.dto.response.ApiResponse;
import com.dcm.backend.dto.response.CreditsResponse;
import com.dcm.backend.dto.response.DayBookRecordsResponse;
import com.dcm.backend.dto.response.PendingTransactionsResponse;
import com.dcm.backend.exception.TransactionsNotFound;

public interface ManagerService {
	ApiResponse<List<PendingTransactionsResponse>> getPendingTransaction()throws TransactionsNotFound;
	ApiResponse<Void> updateTransactionAndAmount(Long tagNumber,Double totalAmount) throws TransactionsNotFound;
	ApiResponse<Map<String,Long>> createNewCreditReport(NewCreditRequest request);
	ApiResponse<Void> createNewPayment(NewPaymentRequest request);
	ApiResponse<Void> deleteTransaction(Long tagNumber);
	ApiResponse<Void> addPOMPayment(PurchaseOldMetalRequest request);
	ApiResponse<Void> addSPMPayment(PurchaseOldMetalRequest request);
	ApiResponse<Void> createNewNormalCredit(Long creditId);
	ApiResponse<List<DayBookRecordsResponse>> getDayBookRecords();
	ApiResponse<Map<String,Object>> getAllCredits(int page,int size);
	ApiResponse<Void> settleCreditByCreditId(Long id,Long amount,boolean flag);
	ApiResponse<List<DayBookRecordsResponse>> getGoldDayBookRecords();
	ApiResponse<List<DayBookRecordsResponse>> getSilverDayBookRecords();
	ApiResponse<List<CreditsResponse>> getCreditsByContactNumber(Long contactNumber);
	ApiResponse<List<CreditsResponse>> getCreditsByCustomerName(String customerName);
	ApiResponse<Void> addEmployeSalaryPayment(AddEmployeeSalaryRequest request);
}
