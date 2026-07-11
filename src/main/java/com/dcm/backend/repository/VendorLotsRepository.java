package com.dcm.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dcm.backend.entity.VendorLotsDetails;

public interface VendorLotsRepository extends JpaRepository<VendorLotsDetails,Long> {
	
}
