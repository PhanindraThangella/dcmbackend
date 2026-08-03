package com.dcm.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dcm.backend.entity.CreditDetails;

public interface CreditDetailsRepository extends JpaRepository<CreditDetails,Long> {
	List<CreditDetails> findByCustomerNameContainingIgnoreCase(String customerName);
	List<CreditDetails> findByContactNumber(Long contactNumber);
}
