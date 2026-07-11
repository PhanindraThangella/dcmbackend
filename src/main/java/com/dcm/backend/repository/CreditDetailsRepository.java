package com.dcm.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dcm.backend.entity.CreditDetails;

public interface CreditDetailsRepository extends JpaRepository<CreditDetails,Long> {
	
}
