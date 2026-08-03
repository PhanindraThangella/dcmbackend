package com.dcm.backend.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dcm.backend.dto.request.AddEmployeeSalaryRequest;
import com.dcm.backend.dto.request.NewCreditRequest;
import com.dcm.backend.dto.request.NewPaymentRequest;
import com.dcm.backend.dto.request.PurchaseOldMetalRequest;
import com.dcm.backend.dto.request.UpdateTransactionRequest;
import com.dcm.backend.dto.response.ApiResponse;
import com.dcm.backend.dto.response.CreditsResponse;
import com.dcm.backend.dto.response.DayBookRecordsResponse;
import com.dcm.backend.dto.response.PendingTransactionsResponse;
import com.dcm.backend.exception.TransactionsNotFound;
import com.dcm.backend.service.ManagerService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/manager")
@RequiredArgsConstructor
public class ManagerServiceController {
	private final ManagerService managerService;
	@GetMapping("/getPendingTransactions")
	public ResponseEntity<ApiResponse<List<PendingTransactionsResponse>>> getPendingTransactionsForManager() throws TransactionsNotFound{
		ApiResponse<List<PendingTransactionsResponse>> response=this.managerService.getPendingTransaction();
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
	@GetMapping("/getDayBookSales")
	public ResponseEntity<ApiResponse<List<DayBookRecordsResponse>>> getDayBookSales() throws TransactionsNotFound{
		ApiResponse<List<DayBookRecordsResponse>> response=this.managerService.getDayBookRecords();
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
	@PutMapping("/updateTransactionStatus")
	public ResponseEntity<ApiResponse<Void>> updateTransactionStatus(@RequestBody UpdateTransactionRequest request ) throws TransactionsNotFound
	{
		ApiResponse<Void> response=this.managerService.updateTransactionAndAmount(request.tagNumber(), request.totalAmount());
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
	@PostMapping("/createCredit")
	public ResponseEntity<ApiResponse<Map<String,Long>>> createNewCreditForCustomer(@RequestBody NewCreditRequest request)
	{
		ApiResponse<Map<String,Long>> response=this.managerService.createNewCreditReport(request);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
	@PostMapping("/addNewPayment")
	public ResponseEntity<ApiResponse<Void>> addNewPayment(@RequestBody NewPaymentRequest request )
	{
		ApiResponse<Void> response=this.managerService.createNewPayment(request);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
	@DeleteMapping("/remove/transaction/{tagNumber}")
	public ResponseEntity<ApiResponse<Void>> deleteTransactionByTagNumber(@PathVariable("tagNumber")Long tagNumber)
	{
		ApiResponse<Void> response=this.managerService.deleteTransaction(tagNumber);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
	@PostMapping("/addPurchaseOldMetalPayment")
	public ResponseEntity<ApiResponse<Void>> addPOMPayment(@RequestBody PurchaseOldMetalRequest request )
	{
		ApiResponse<Void> response=this.managerService.addPOMPayment(request);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
	@PostMapping("/addSellPureMetalPayment")
	public ResponseEntity<ApiResponse<Void>> addSPMPayment(@RequestBody PurchaseOldMetalRequest request )
	{
		ApiResponse<Void> response=this.managerService.addSPMPayment(request);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
	@PostMapping("/newNormalCredit/{creditId}")
	public ResponseEntity<ApiResponse<Void>> normalCreditCreate(@PathVariable("creditId") Long creditId)
	{
		ApiResponse<Void> response=this.managerService.createNewNormalCredit(creditId);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
	@GetMapping("/getCreditHistory")
	public ResponseEntity<ApiResponse<Map<String,Object>>> getCreditsOfCustomers(@RequestParam(defaultValue = "0")int page ,
			@RequestParam(defaultValue = "10")int size)
	{
		ApiResponse<Map<String,Object>> response=this.managerService.getAllCredits(page, size);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
	@PutMapping("/settleCreditOfCustomer/{id}/{amount}/{flag}")
	public ResponseEntity<ApiResponse<Void>> settleCredit(@PathVariable("id")Long id,@PathVariable("amount")Long amount,
			@PathVariable("flag")boolean flag)
	{
		ApiResponse<Void> response=this.managerService.settleCreditByCreditId(id, amount,flag);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
	@GetMapping("/getGoldDayBook")
	public ResponseEntity<ApiResponse<List<DayBookRecordsResponse>>> getGoldDayBook(){
		ApiResponse<List<DayBookRecordsResponse>>response=this.managerService.getGoldDayBookRecords();
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
	@GetMapping("/getSilverDayBook")
	public ResponseEntity<ApiResponse<List<DayBookRecordsResponse>>> getSilverDayBook(){
		ApiResponse<List<DayBookRecordsResponse>>response=this.managerService.getSilverDayBookRecords();
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
	@GetMapping("/getCreditDetailsContactNumber/{contactNumber}")
	public ResponseEntity<ApiResponse<List<CreditsResponse>>> getCreditsByContactNumber(@PathVariable("contactNumber")Long contactNumber)
	{
		ApiResponse<List<CreditsResponse>> response=this.managerService.getCreditsByContactNumber(contactNumber);
		return ResponseEntity.status(HttpStatus.OK).body(response);	
	}
	@GetMapping("/getCreditDetailsCustomerName/{customerName}")
	public ResponseEntity<ApiResponse<List<CreditsResponse>>> getCreditsByCustomerName(@PathVariable("customerName")String customerName)
	{
		ApiResponse<List<CreditsResponse>> response=this.managerService.getCreditsByCustomerName(customerName);
		return ResponseEntity.status(HttpStatus.OK).body(response);	
	}
	@PostMapping("/addEmployeeSalaryPayment")
	public ResponseEntity<ApiResponse<Void>> addEmployeeSalary(@RequestBody AddEmployeeSalaryRequest request )
	{
		ApiResponse<Void> response=this.managerService.addEmployeSalaryPayment(request);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
}
