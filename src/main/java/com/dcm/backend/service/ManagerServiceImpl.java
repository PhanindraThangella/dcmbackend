package com.dcm.backend.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dcm.backend.dto.request.AddEmployeeSalaryRequest;
import com.dcm.backend.dto.request.NewCreditRequest;
import com.dcm.backend.dto.request.NewPaymentRequest;
import com.dcm.backend.dto.request.PurchaseOldMetalRequest;
import com.dcm.backend.dto.response.ApiResponse;
import com.dcm.backend.dto.response.CreditsResponse;
import com.dcm.backend.dto.response.DayBookRecordsResponse;
import com.dcm.backend.dto.response.PendingTransactionsResponse;
import com.dcm.backend.entity.CreditDetails;
import com.dcm.backend.entity.DayPurse;
import com.dcm.backend.entity.JewelleryItems;
import com.dcm.backend.entity.PaymentDetails;
import com.dcm.backend.entity.Transactions;
import com.dcm.backend.enums.ItemType;
import com.dcm.backend.enums.TransactionStatus;
import com.dcm.backend.exception.ResourceNotFoundException;
import com.dcm.backend.exception.TransactionsNotFound;
import com.dcm.backend.repository.CreditDetailsRepository;
import com.dcm.backend.repository.DayPurseRepository;
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
	private final DayPurseRepository dayPurse;
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
		if(Integer.parseInt(request.creditId())!=0) {
			Optional<CreditDetails> creditResponse=creditDetailsRepository.findById(Long.parseLong(request.creditId()));
			newPayment.setCreditAmount(creditResponse.get().getTotalAmount());
		}
		else {
			newPayment.setCreditAmount(0L);
		}
		if(request.itemType().toUpperCase().equals("GOLD"))
		{
			newPayment.setOldGoldGrams(request.ogGrams());
			newPayment.setOldGoldAmount(request.ogAmount());
		}
		else if(request.itemType().toUpperCase().equals("SILVER"))
		{
			newPayment.setOldSilverGrams(request.ogGrams());
			newPayment.setOldSilverAmount(request.ogAmount());
		}
		newPayment.setCreditId(Long.parseLong(request.creditId()));
		this.paymentDetailsRepository.save(newPayment);
		LocalDate today = LocalDate.now();
        LocalDateTime startOfToday = today.atStartOfDay(); 
        LocalDateTime endOfToday = today.atTime(LocalTime.MAX); 
		Optional<DayPurse> purse=this.dayPurse.findTopByCreatedAtBetweenOrderByIdDesc(startOfToday, endOfToday);
		DayPurse pp=purse.orElseThrow(()-> new ResourceNotFoundException("Purse Empty Unable To Proceed."));
		pp.setCurrentPurse(pp.getCurrentPurse()+(request.totalCash()+request.totalUpi()+request.ogAmount()));
		this.dayPurse.save(pp);
		Optional<JewelleryItems> item =this.itemsRepository.findByTagNumber(Long.parseLong(request.tagNumber()));
		JewelleryItems itemToDelete=item.orElseThrow(()->new ResourceNotFoundException("Item not found to remove"));
		this.itemsRepository.delete(itemToDelete);
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
		LocalDate today = LocalDate.now();
        LocalDateTime startOfToday = today.atStartOfDay(); 
        LocalDateTime endOfToday = today.atTime(LocalTime.MAX); 
		Optional<DayPurse> purse=this.dayPurse.findTopByCreatedAtBetweenOrderByIdDesc(startOfToday, endOfToday);
		DayPurse pp=purse.orElseThrow(()-> new ResourceNotFoundException("Purse Empty Unable To Proceed."));
		pp.setCurrentPurse(pp.getCurrentPurse()-(request.amount()));
		this.dayPurse.save(pp);
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
		LocalDate today = LocalDate.now();
        LocalDateTime startOfToday = today.atStartOfDay(); 
        LocalDateTime endOfToday = today.atTime(LocalTime.MAX); 
		Optional<DayPurse> purse=this.dayPurse.findTopByCreatedAtBetweenOrderByIdDesc(startOfToday, endOfToday);
		DayPurse pp=purse.orElseThrow(()-> new ResourceNotFoundException("Purse Empty Unable To Proceed."));
		pp.setCurrentPurse(pp.getCurrentPurse()+(request.amount()));
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
		List<PaymentDetails> fetchedList=this.paymentDetailsRepository.findByCreatedAtBetweenOrderByCreatedAtDesc(startOfToday, endOfToday);
		List<DayBookRecordsResponse> result=new ArrayList<DayBookRecordsResponse>();
		for(PaymentDetails t:fetchedList)
		{
			
			if(t.getTagNumber()!=0)
			{
				Transactions tran=this.transactionRepository.findByTagNumber(t.getTagNumber());
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
							.creditAmount(t.getCreditAmount())
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
							.creditAmount(t.getCreditAmount())
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
			else if(t.getTagNumber()==0 && !t.getEmployeeName().equals("no"))
			{
				DayBookRecordsResponse res=DayBookRecordsResponse.builder()
						.typeOfPayment("Employee Salary")
						.employeeName(t.getEmployeeName())
						.totalAmount(t.getTotalCash())
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
	@Override
	public ApiResponse<Void> settleCreditByCreditId(Long id,Long amount,boolean flag) {
		Optional<CreditDetails> credit=this.creditDetailsRepository.findById(id);
		CreditDetails customerCreditDetails=credit.orElseThrow(()->new ResourceNotFoundException("Credit for given customer not found."));
		String msg="";
		if(amount.equals(customerCreditDetails.getTotalAmount()) || flag)
		{
			this.creditDetailsRepository.delete(customerCreditDetails);
			msg+="Total Credit Settled.";
		}
		else
		{
			customerCreditDetails.setTotalAmount(customerCreditDetails.getTotalAmount()-amount);
			this.creditDetailsRepository.save(customerCreditDetails);
			msg+="Credit Has Updated.";
		}
		LocalDate today = LocalDate.now();
        LocalDateTime startOfToday = today.atStartOfDay(); 
        LocalDateTime endOfToday = today.atTime(LocalTime.MAX); 
		Optional<DayPurse> purse=this.dayPurse.findTopByCreatedAtBetweenOrderByIdDesc(startOfToday, endOfToday);
		DayPurse pp=purse.orElseThrow(()-> new ResourceNotFoundException("Purse Empty Unable To Proceed."));
		pp.setCurrentPurse(pp.getCurrentPurse()+amount);	
		return ApiResponse.<Void>builder()
				.success(true)
				.message(msg)
				.data(null)
				.timestamp(LocalDateTime.now())
				.build();
	}
	@Override
	public ApiResponse<Map<String, Object>> getAllCredits(int page, int size) {
		 // 1. Create a pageable configuration object
        Pageable pageable = PageRequest.of(page, size,Sort.by(Sort.Direction.DESC, "createdAt"));
        
        // 2. Fetch the slice of data from database
        Page<CreditDetails> creditPage = this.creditDetailsRepository.findAll(pageable);

        // 3. Format the response object mapping data + metadata
        Map<String, Object> response = new HashMap<>();
        response.put("data", creditPage.getContent());
        response.put("currentPage", creditPage.getNumber());
        response.put("totalItems", creditPage.getTotalElements());
        response.put("totalPages", creditPage.getTotalPages());
        response.put("isFirst", creditPage.isFirst());
        response.put("isLast", creditPage.isLast());
		return ApiResponse.<Map<String,Object>>builder()
				.success(true)
				.message("Succesfully fetched credits of "+creditPage.getTotalElements()+" people in page:"+creditPage.getNumber())
				.data(response)
				.timestamp(LocalDateTime.now() )
				.build();
	}
	@Override
	public ApiResponse<List<DayBookRecordsResponse>> getGoldDayBookRecords() {
		LocalDate today = LocalDate.now();
        LocalDateTime startOfToday = today.atStartOfDay(); 
        LocalDateTime endOfToday = today.atTime(LocalTime.MAX); 
		List<PaymentDetails> fetchedList=this.paymentDetailsRepository.findByCreatedAtBetweenOrderByCreatedAtDesc(startOfToday, endOfToday);
		List<DayBookRecordsResponse> result=new ArrayList<DayBookRecordsResponse>();
		for(PaymentDetails t:fetchedList)
		{
			
			if(t.getTagNumber()!=0)
			{
				Transactions tran=this.transactionRepository.findByTagNumber(t.getTagNumber());
				if(tran.getItemType().equals(ItemType.GOLD))
				{
					DayBookRecordsResponse res=DayBookRecordsResponse.builder()
							.typeOfPayment("GOLD")
							.tagNumber(t.getTagNumber())
							.totalGrams(tran.getTotalGrams())
							.totalAmount(Long.parseLong(tran.getTotalAmount().toString().split("\\.")[0]))
							.build();
					result.add(res);
				}
			}
			else if(t.getTagNumber()==0 && t.getOldGoldAmount()>0 && t.getOldGoldGrams()>0.0)
			{
				DayBookRecordsResponse res=DayBookRecordsResponse.builder()
						.typeOfPayment("OLD GOLD")
						.totalGrams(t.getOldGoldGrams())
						.totalAmount(t.getOldGoldAmount())
						.build();
				result.add(res);
			}
			else if(t.getTagNumber()==0 && t.getPureGoldAmount()>0 && t.getPureGoldGrams()>0.0)
			{
				DayBookRecordsResponse res=DayBookRecordsResponse.builder()
						.typeOfPayment("PURE GOLD")
						.totalGrams(t.getPureGoldGrams())
						.totalAmount(t.getPureGoldAmount())
						.build();
				result.add(res);
			}
		}
		return ApiResponse.<List<DayBookRecordsResponse>>builder()
				.success(true)
				.message("Gold DayBook fetched successfully.")
				.data(result)
				.timestamp(LocalDateTime.now())
				.build();
	}
	@Override
	public ApiResponse<List<DayBookRecordsResponse>> getSilverDayBookRecords() {
		LocalDate today = LocalDate.now();
        LocalDateTime startOfToday = today.atStartOfDay(); 
        LocalDateTime endOfToday = today.atTime(LocalTime.MAX); 
		List<PaymentDetails> fetchedList=this.paymentDetailsRepository.findByCreatedAtBetweenOrderByCreatedAtDesc(startOfToday, endOfToday);
		List<DayBookRecordsResponse> result=new ArrayList<DayBookRecordsResponse>();
		for(PaymentDetails t:fetchedList)
		{
			
			if(t.getTagNumber()!=0)
			{
				Transactions tran=this.transactionRepository.findByTagNumber(t.getTagNumber());
				if(tran.getItemType().equals(ItemType.SILVER))
				{
					DayBookRecordsResponse res=DayBookRecordsResponse.builder()
							.typeOfPayment("SILVER")
							.tagNumber(t.getTagNumber())
							.totalGrams(tran.getTotalGrams())
							.totalAmount(Long.parseLong(tran.getTotalAmount().toString().split("\\.")[0]))
							.build();
					result.add(res);
				}
			}
			else if(t.getTagNumber()==0 && t.getOldSilverAmount()>0 && t.getOldSilverGrams()>0.0)
			{
				DayBookRecordsResponse res=DayBookRecordsResponse.builder()
						.typeOfPayment("OLD SILVER")
						.totalGrams(t.getOldSilverGrams())
						.totalAmount(t.getOldSilverAmount())
						.build();
				result.add(res);
			}
			else if(t.getTagNumber()==0 && t.getPureSilverAmount()>0 && t.getPureSilverGrams()>0.0)
			{
				DayBookRecordsResponse res=DayBookRecordsResponse.builder()
						.typeOfPayment("PURE SILVER")
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
	@Override
	public ApiResponse<List<CreditsResponse>> getCreditsByContactNumber(Long contactNumber) {
		List<CreditDetails> fetchedList=this.creditDetailsRepository.findByContactNumber(contactNumber);
		if(fetchedList.isEmpty())
			throw new ResourceNotFoundException("No Credits Found with matching Number");
		List<CreditsResponse> result=new ArrayList<CreditsResponse>();
		for(CreditDetails c:fetchedList)
		{
			CreditsResponse cr=CreditsResponse.builder()
					.customerName(c.getCustomerName())
					.totalAmount(c.getTotalAmount())
					.contactNumber(c.getContactNumber())
					.id(c.getId())
					.build();
			result.add(cr);
		}
		return ApiResponse.<List<CreditsResponse>>builder()
				.success(true)
				.message("Fetched Successfully")
				.data(result)
				.timestamp(LocalDateTime.now())
				.build();
	}
	@Override
	public ApiResponse<List<CreditsResponse>> getCreditsByCustomerName(String customerName) {
		List<CreditDetails> fetchedList=this.creditDetailsRepository.findByCustomerNameContainingIgnoreCase(customerName);
		if(fetchedList.isEmpty())
			throw new ResourceNotFoundException("No Credits Found with matching Name");
		List<CreditsResponse> result=new ArrayList<CreditsResponse>();
		for(CreditDetails c:fetchedList)
		{
			CreditsResponse cr=CreditsResponse.builder()
					.customerName(c.getCustomerName())
					.totalAmount(c.getTotalAmount())
					.contactNumber(c.getContactNumber())
					.id(c.getId())
					.build();
			result.add(cr);
		}
		return ApiResponse.<List<CreditsResponse>>builder()
				.success(true)
				.message("Fetched Successfully")
				.data(result)
				.timestamp(LocalDateTime.now())
				.build();
	}
	@Override
	public ApiResponse<Void> addEmployeSalaryPayment(AddEmployeeSalaryRequest request) {
		PaymentDetails payment=PaymentDetails.builder()
				.employeeName(request.employeeName())
				.totalCash(request.amount())
				.build();
		LocalDate today = LocalDate.now();
        LocalDateTime startOfToday = today.atStartOfDay(); 
        LocalDateTime endOfToday = today.atTime(LocalTime.MAX); 
		Optional<DayPurse> purse=this.dayPurse.findTopByCreatedAtBetweenOrderByIdDesc(startOfToday, endOfToday);
		DayPurse pp=purse.orElseThrow(()-> new ResourceNotFoundException("Purse Empty Unable To Proceed."));
		pp.setCurrentPurse(pp.getCurrentPurse()-(request.amount()));
		this.paymentDetailsRepository.save(payment);
		return ApiResponse.<Void>builder()
				.success(true)
				.message("Added Payment Successfully")
				.data(null)
				.timestamp(LocalDateTime.now())
				.build();
	}
}
