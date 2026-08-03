package com.dcm.backend.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dcm.backend.dto.request.TagGenerateRequest;
import com.dcm.backend.dto.response.ApiResponse;
import com.dcm.backend.dto.response.ItemDetailsResponse;
import com.dcm.backend.dto.response.VerifyItemsResponse;
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

	@Override
	public ApiResponse<ItemDetailsResponse> fetchTagDetails(Long tagNumber) {
		Optional<JewelleryItems>item=this.ItemRepository.findByTagNumber(tagNumber);
		JewelleryItems fetcheditem=item.orElseThrow(()->new ResourceNotFoundException("Item not found with tagnumber."));
		ItemDetailsResponse response=ItemDetailsResponse.builder()
				.grossWeight(fetcheditem.getGrossWeight())
				.netWeight(fetcheditem.getNetWeight())
				.peices(fetcheditem.getNoOfPeices())
				.ProductName(fetcheditem.getProductName())
				.stoneWeight(fetcheditem.getStoneWeight())
				.stoneAmount(fetcheditem.getStoneAmount())
				.id(fetcheditem.getId())
				.build();
		return ApiResponse.<ItemDetailsResponse>builder()
				.success(true)
				.message("Item Fetched Successfully")
				.data(response)
				.timestamp(LocalDateTime.now())
				.build();
	}

	@Override
	public ApiResponse<Void> UpdateTagDetails(TagGenerateRequest request) {
		Optional<JewelleryItems>fetchedItem=this.ItemRepository.findByTagNumber(request.tagNumber());
		JewelleryItems item=fetchedItem.orElseThrow(()->new ResourceNotFoundException("Item not found with tagnumber."));
		item.setGrossWeight(request.grossWeight());
		item.setNetWeight(request.netWeight());
		item.setNoOfPeices(request.peices());
		item.setStoneAmount(request.stoneAmount());
		item.setStoneWeight(request.stoneWeight());
		this.ItemRepository.save(item);
		return ApiResponse.<Void>builder()
				.success(true)
				.message("Updated Details Successfully.")
				.data(null)
				.timestamp(LocalDateTime.now())
				.build();
	}

	@Override
	public ApiResponse<List<VerifyItemsResponse>> getTagsByProductName(String productName) {
		List<JewelleryItems> items=this.ItemRepository.findByProductName(productName);
		List<VerifyItemsResponse> response=new ArrayList<VerifyItemsResponse>();
		for(JewelleryItems item:items)
		{
			VerifyItemsResponse vt=VerifyItemsResponse.builder()
					.tagNumber(item.getTagNumber())
					.peices(item.getNoOfPeices())
					.grossWeight(item.getGrossWeight())
					.netWeight(item.getNetWeight())
					.build();
			response.add(vt);
		}
		return ApiResponse.<List<VerifyItemsResponse>>builder()
				.success(true)
				.message("items fetched succesfully.")
				.data(response)
				.timestamp(LocalDateTime.now())
				.build();
	}

	@Override
	public ApiResponse<Void> deleteItem(Long id) {
		this.ItemRepository.deleteById(id);
		return ApiResponse.<Void>builder()
				.success(true)
				.message("Item deleted successfully")
				.data(null)
				.timestamp(LocalDateTime.now())
				.build();
	}

	@Override
	public ApiResponse<List<ItemDetailsResponse>> getLastRecentItems() {
		List<JewelleryItems> fetchedList=this.ItemRepository.findTop20ByOrderByIdDesc();
		List<ItemDetailsResponse> result=new ArrayList<>();
		for(JewelleryItems item:fetchedList)
		{
			ItemDetailsResponse itemDetails=ItemDetailsResponse.builder()
					.tagNumber(item.getTagNumber())
					.ProductName(item.getProductName())
					.grossWeight(item.getGrossWeight())
					.netWeight(item.getNetWeight())
					.peices(item.getNoOfPeices())
					.stoneWeight(item.getStoneWeight())
					.stoneAmount(item.getStoneAmount())
					.itemType(item.getMainProduct())
					.build();
			result.add(itemDetails);
		}
		return ApiResponse.<List<ItemDetailsResponse>>builder()
				.success(true)
				.message("Recent items fetched successfully.")
				.data(result)
				.timestamp(LocalDateTime.now())
				.build();
	}
}
