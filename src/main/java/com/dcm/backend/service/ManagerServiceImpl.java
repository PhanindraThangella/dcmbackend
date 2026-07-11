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

import com.dcm.backend.dto.request.NewCreditRequest;
import com.dcm.backend.dto.request.NewPaymentRequest;
import com.dcm.backend.dto.request.PurchaseOldMetalRequest;
import com.dcm.backend.dto.response.ApiResponse;
import com.dcm.backend.dto.response.DayBookRecordsResponse;
import com.dcm.backend.dto.response.PendingTransactionsResponse;
import com.dcm.backend.entity.CreditDetails;
import com.dcm.backend.entity.PaymentDetails;
import com.dcm.backend.entity.Transactions;
import com.dcm.backend.enums.ItemType;
import com.dcm.backend.enums.TransactionStatus;
import com.dcm.backend.exception.TransactionsNotFound;
import com.dcm.backend.repository.CreditDetailsRepository;
import com.dcm.backend.repository.JewelleryItemsRepository;
import com.dcm.backend.repository.PaymentDetailsRepository;
import com.dcm.backend.repository.TransactionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ManagerServiceImpl implements ManagerService{
	private final TransactionRepository transactionRepository;
	private final CreditDetailsRepository creditDetailsRepository;
	private final PaymentDetailsRepository paymentDetailsRepository;
	private final JewelleryItemsRepository itemsRepository;
	@Override
	public ApiResponse<List<PendingTransactionsResponse>> getPendingTransaction() throws TransactionsNotFound {
		List<Transactions> transactions=this.transactionRepository.fetchPendingTransaction();
		if(transactions.size()==0)
			throw new TransactionsNotFound("No Pending transactions currently");
		List<PendingTransactionsResponse> result=new ArrayList<>();
		for(Transactions s:transactions)
		{
			result.add(PendingTransactionsResponse.builder()
					.itemType(s.getItemType())
					.tagNumber(s.getTagNumber())
					.totalGrams(s.getTotalGrams())
					.ratePerGram(s.getRatePerGram())
					.totalAmount(s.getTotalAmount())
					.wastage(s.getWastage())
					.makingCharges(s.getMakingCharges())
					.build());
			
		}
		return ApiResponse.<List<PendingTransactionsResponse>>builder()
				.success(true)
				.message("Successfully fetched pending Transactions.")
				.data(result)
				.timestamp(LocalDateTime.now())
				.build();
	}
	@Override
	public ApiResponse<Void> updateTransactionAndAmount(Long tagNumber,Double totalAmount) throws TransactionsNotFound {
		Transactions transaction=this.transactionRepository.findByTagNumber(tagNumber);
		String msg="";
		if(totalAmount>0 && transaction.getTotalAmount()>totalAmount)
		{
			transaction.setTotalAmount(totalAmount);
			msg+="Total Amount and";
		}
		transaction.setTransactionStatus(TransactionStatus.VERIFIED);
		return ApiResponse.<Void>builder()
				.success(true)
				.message(msg+"Status Updated.")
				.data(null)
				.timestamp(LocalDateTime.now())
				.build();
	}
	@Override
	public ApiResponse<Map<String, Long>> createNewCreditReport(NewCreditRequest request) {
		CreditDetails creditDetails=new CreditDetails();
		creditDetails.setCustomerName(request.customerName());
		creditDetails.setContactNumber(request.mobileNumber());
		creditDetails.setTotalAmount(request.creditAmount());
		Long id=creditDetailsRepository.save(creditDetails).getId();
		Map<String,Long> response=new HashMap<String, Long>();
		response.put("creditId", id);
		return ApiResponse.<Map<String,Long>>builder()
				.success(true)
				.message("Created New Credit For Customer.")
				.data(response)
				.timestamp(LocalDateTime.now())
				.build();
	}
	@Override
	public ApiResponse<Void> createNewPayment(NewPaymentRequest request) {
		PaymentDetails newPayment=new PaymentDetails();
		newPayment.setTagNumber(Long.parseLong(request.tagNumber()));
		newPayment.setTotalCash(request.totalCash());
		newPayment.setTotalUpiAmount(request.totalUpi());
		newPayment.setOldGoldGrams(request.ogGrams());
		newPayment.setOldGoldAmount(request.ogAmount());
		newPayment.setCreditId(Long.parseLong(request.creditId()));
		this.paymentDetailsRepository.save(newPayment);
		return ApiResponse.<Void>builder()
				.success(true)
				.message("Payment Added Successfully")
				.data(null)
				.timestamp(LocalDateTime.now())
				.build();
	}
	@Override
	public ApiResponse<Void> deleteTransaction(Long tagNumber) {
		int count=this.transactionRepository.deleteByTagNumber(tagNumber);
		String msg="";
		if(count>=1)
		{
			msg="Successfully deleted "+count+" Records from transactions";
		}
		return ApiResponse.<Void>builder()
				.success(true)
				.message(msg)
				.data(null)
				.timestamp(LocalDateTime.now())
				.build();
	}
	@Override
	public ApiResponse<Void> addPOMPayment(PurchaseOldMetalRequest request) {
		if(request.selectedMetal().toUpperCase().equals("GOLD"))
		{
			PaymentDetails payment=PaymentDetails.builder()
					.oldGoldGrams(request.weight())
					.oldGoldAmount(request.amount())
					.build();
			this.paymentDetailsRepository.save(payment);
		}
		else if(request.selectedMetal().toUpperCase().equals("SILVER"))
		{
			PaymentDetails payment=PaymentDetails.builder()
					.oldSilverGrams(request.weight())
					.oldSilverAmount(request.amount())
					.build();
			this.paymentDetailsRepository.save(payment);
		}
		return ApiResponse.<Void>builder()
				.success(true)
				.message("Payment Added Successfully")
				.data(null)
				.timestamp(LocalDateTime.now())
				.build();
	}
	@Override
	public ApiResponse<Void> addSPMPayment(PurchaseOldMetalRequest request) {
		if(request.selectedMetal().toUpperCase().equals("PUREGOLD"))
		{
			PaymentDetails payment=PaymentDetails.builder()
					.pureGoldGrams(request.weight())
					.pureGoldAmount(request.amount())
					.build();
			this.paymentDetailsRepository.save(payment);
		}
		else if(request.selectedMetal().toUpperCase().equals("PURESILVER"))
		{
			PaymentDetails payment=PaymentDetails.builder()
					.pureSilverGrams(request.weight())
					.pureSilverAmount(request.amount())
					.build();
			this.paymentDetailsRepository.save(payment);
		}
		return ApiResponse.<Void>builder()
				.success(true)
				.message("Payment Added Successfully")
				.data(null)
				.timestamp(LocalDateTime.now())
				.build();
	}
	@Override
	public ApiResponse<Void> createNewNormalCredit(Long creditId) {
		PaymentDetails payment=PaymentDetails.builder()
				.creditId(creditId)
				.build();
		this.paymentDetailsRepository.save(payment);
		return ApiResponse.<Void>builder()
				.success(true)
				.message("Credit added to payment successfully")
				.data(null)
				.timestamp(LocalDateTime.now())
				.build();
	}
	@Override
	public ApiResponse<List<DayBookRecordsResponse>> getDayBookRecords() {
		LocalDate today = LocalDate.now();
        LocalDateTime startOfToday = today.atStartOfDay(); 
        LocalDateTime endOfToday = today.atTime(LocalTime.MAX); 
		List<PaymentDetails> fetchedList=this.paymentDetailsRepository.findByCreatedAtBetween(startOfToday, endOfToday);
		List<DayBookRecordsResponse> result=new ArrayList<DayBookRecordsResponse>();
		for(PaymentDetails t:fetchedList)
		{
			
			if(t.getTagNumber()!=0)
			{
				Transactions tran=this.transactionRepository.findByTagNumber(t.getTagNumber());
				Long creditAmount=0L;
				if(t.getCreditId()>0)
				{
					Optional<CreditDetails> cred=this.creditDetailsRepository.findById(t.getCreditId());
					creditAmount=cred.get().getTotalAmount();
				}
				if(tran.getItemType().equals(ItemType.GOLD))
				{
					DayBookRecordsResponse res=DayBookRecordsResponse.builder()
							.typeOfPayment("Sales")
							.tagNumber(t.getTagNumber())
							.totalGrams(tran.getTotalGrams())
							.totalAmount(Long.parseLong(tran.getTotalAmount().toString().split("\\.")[0]))
							.cashPaid(t.getTotalCash())
							.upiPaid(t.getTotalUpiAmount())
							.oldGoldGrams(t.getOldGoldGrams())
							.oldGoldAmount(t.getOldGoldAmount())
							.itemType(tran.getItemType().toString())
							.creditAmount(creditAmount)
							.build();
					result.add(res);
				}
				else if(tran.getItemType().equals(ItemType.SILVER))
				{
					DayBookRecordsResponse res=DayBookRecordsResponse.builder()
							.typeOfPayment("Sales")
							.tagNumber(t.getTagNumber())
							.totalGrams(tran.getTotalGrams())
							.totalAmount(Long.parseLong(tran.getTotalAmount().toString().split("\\.")[0]))
							.cashPaid(t.getTotalCash())
							.upiPaid(t.getTotalUpiAmount())
							.oldSilverGrams(t.getOldSilverGrams())
							.oldSilverAmount(t.getOldSilverAmount())
							.itemType(tran.getItemType().toString())
							.creditAmount(creditAmount)
							.build();
					result.add(res);
				}
			}
			else if(t.getCreditId()>0 && t.getTagNumber()==0)
			{
				Optional<CreditDetails> credit=this.creditDetailsRepository.findById(t.getCreditId());
				DayBookRecordsResponse res=DayBookRecordsResponse.builder()
						.typeOfPayment("Credit")
						.customerName(credit.get().getCustomerName())
						.mobileNumber(credit.get().getContactNumber())
						.creditAmount(credit.get().getTotalAmount())
						.build();
				result.add(res);
			}
			else if(t.getTagNumber()==0 && t.getOldGoldAmount()>0 && t.getOldGoldGrams()>0.0)
			{
				DayBookRecordsResponse res=DayBookRecordsResponse.builder()
						.typeOfPayment("OG Purchase")
						.totalGrams(t.getOldGoldGrams())
						.totalAmount(t.getOldGoldAmount())
						.build();
				result.add(res);
			}
			else if(t.getTagNumber()==0 && t.getOldSilverAmount()>0 && t.getOldSilverGrams()>0.0)
			{
				DayBookRecordsResponse res=DayBookRecordsResponse.builder()
						.typeOfPayment("OS Purchase")
						.totalGrams(t.getOldSilverGrams())
						.totalAmount(t.getOldSilverAmount())
						.build();
				result.add(res);
			}
			else if(t.getTagNumber()==0 && t.getPureGoldAmount()>0 && t.getPureGoldGrams()>0.0)
			{
				DayBookRecordsResponse res=DayBookRecordsResponse.builder()
						.typeOfPayment("PG Sale")
						.totalGrams(t.getPureGoldGrams())
						.totalAmount(t.getPureGoldAmount())
						.build();
				result.add(res);
			}
			else if(t.getTagNumber()==0 && t.getPureSilverAmount()>0 && t.getPureSilverGrams()>0.0)
			{
				DayBookRecordsResponse res=DayBookRecordsResponse.builder()
						.typeOfPayment("PS Sale")
						.totalGrams(t.getPureSilverGrams())
						.totalAmount(t.getPureSilverAmount())
						.build();
				result.add(res);
			}
		}
		return ApiResponse.<List<DayBookRecordsResponse>>builder()
				.success(true)
				.message("Day Book fetched successfully.")
				.data(result)
				.timestamp(LocalDateTime.now())
				.build();
	}
}
