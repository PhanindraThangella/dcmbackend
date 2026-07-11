package com.dcm.backend.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dcm.backend.dto.request.TransactionRegRequest;
import com.dcm.backend.dto.request.TransactionUpdateRequest;
import com.dcm.backend.dto.response.ApiResponse;
import com.dcm.backend.dto.response.TransactionResponse;
import com.dcm.backend.exception.TransactionsNotFound;
import com.dcm.backend.service.TransactionService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
@Validated
public class TransactionController {
	private final TransactionService transactionService;
	@PostMapping("/addTransaction")
	public ResponseEntity<ApiResponse<Void>> addTransactionToDataBase(@Valid @RequestBody TransactionRegRequest request)
	{
		ApiResponse<Void> response=transactionService.addTransaction(request);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
	@GetMapping("/getTransactions/{id}")
	public ResponseEntity<ApiResponse<List<TransactionResponse>>> getAllTransactions(@PathVariable("id") String id) throws TransactionsNotFound
	{
		ApiResponse<List<TransactionResponse>> response=transactionService.getAllTransactionsOnDate(id);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
	@PostMapping("/updateTransaction")
	public ResponseEntity<ApiResponse<Void>> updateTransaction(@Valid @RequestBody TransactionUpdateRequest request) throws TransactionsNotFound
	{
		ApiResponse<Void> response=this.transactionService.updateTransaction(request);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
}
