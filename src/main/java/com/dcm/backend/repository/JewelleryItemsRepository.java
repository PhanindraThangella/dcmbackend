package com.dcm.backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dcm.backend.entity.JewelleryItems;

public interface JewelleryItemsRepository extends JpaRepository<JewelleryItems,Long> {
	Optional<JewelleryItems> findTopByMainProductOrderByIdDesc(String mainProduct);
	Optional<JewelleryItems> findByTagNumber(Long tagNumber);
}
