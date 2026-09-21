package com.dcm.backend.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dcm.backend.entity.PaymentDetails;

public interface PaymentDetailsRepository extends JpaRepository<PaymentDetails,Long> {
	List<PaymentDetails> findByCreatedAtBetweenOrderByCreatedAtDesc(LocalDateTime start,LocalDateTime end);
}
