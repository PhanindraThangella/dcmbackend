package com.dcm.backend.service;


import java.util.List;

import com.dcm.backend.dto.request.TransactionRegRequest;
import com.dcm.backend.dto.request.TransactionUpdateRequest;
import com.dcm.backend.dto.response.ApiResponse;
import com.dcm.backend.dto.response.TransactionResponse;
import com.dcm.backend.exception.TransactionsNotFound;

public interface TransactionService{
	ApiResponse<List<TransactionResponse>> getAllTransactionsOnDate(String employeeId) throws TransactionsNotFound;
	ApiResponse<Void> addTransaction(TransactionRegRequest transactionRequest);
	ApiResponse<Void> updateTransaction(TransactionUpdateRequest transactionUpdateRequest) throws TransactionsNotFound;
}
