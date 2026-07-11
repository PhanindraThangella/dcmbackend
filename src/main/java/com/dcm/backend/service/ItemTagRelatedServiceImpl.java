package com.dcm.backend.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dcm.backend.dto.request.TagGenerateRequest;
import com.dcm.backend.dto.response.ApiResponse;
import com.dcm.backend.entity.JewelleryItems;
import com.dcm.backend.entity.VendorLotsDetails;
import com.dcm.backend.exception.ResourceNotFoundException;
import com.dcm.backend.repository.JewelleryItemsRepository;
import com.dcm.backend.repository.VendorLotsRepository;
import com.dcm.backend.util.TagNumberGenerator;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ItemTagRelatedServiceImpl implements ItemTagRelatedService
{	
	private final JewelleryItemsRepository ItemRepository;
	private final VendorLotsRepository vendorLotsRepository;
	private final TagNumberGenerator tagNumberGenerator;
	
	@Override
	public ApiResponse<Void> createNewTag(TagGenerateRequest request) {
		Optional<VendorLotsDetails> optional=this.vendorLotsRepository.findById(request.lotNo());
		VendorLotsDetails lot=optional.orElseThrow(()->new ResourceNotFoundException("No Lot found with given lot Id!."));
		lot.setGrossWeight(lot.getGrossWeight()-request.grossWeight());
		lot.setNetWeight(lot.getNetWeight()-request.netWeight());
		lot.setPeices(lot.getPeices()-request.peices());
//		lot.setStoneAmount(lot.getStoneAmount()-request.stoneAmount());
		Long tagNumber=tagNumberGenerator.generateTagNumber(request.metalType());
		JewelleryItems item=JewelleryItems.builder()
				.lotNo(request.lotNo())
				.mainProduct(request.metalType())
				.productName(request.productName())
				.noOfPeices(request.peices())
				.grossWeight(request.grossWeight())
				.netWeight(request.netWeight())
				.stoneAmount(request.stoneAmount() != null?request.stoneAmount():0L)
				.stoneWeight(request.stoneWeight() != null?request.stoneWeight():0.0)
				.tagNumber(tagNumber)
				.build();
		this.ItemRepository.save(item);
		this.vendorLotsRepository.save(lot);
		return ApiResponse.<Void>builder()
				.success(true)
				.message("Tag Generated With ID: "+tagNumber)
				.data(null)
				.timestamp(LocalDateTime.now())
				.build();
	}
}
