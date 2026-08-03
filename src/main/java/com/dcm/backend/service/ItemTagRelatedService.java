package com.dcm.backend.service;

import java.util.List;

import com.dcm.backend.dto.request.TagGenerateRequest;
import com.dcm.backend.dto.response.ApiResponse;
import com.dcm.backend.dto.response.ItemDetailsResponse;
import com.dcm.backend.dto.response.VerifyItemsResponse;

public interface ItemTagRelatedService {
	ApiResponse<Void> createNewTag(TagGenerateRequest  request);
	ApiResponse<ItemDetailsResponse> fetchTagDetails(Long tagNumber);
	ApiResponse<Void> UpdateTagDetails(TagGenerateRequest request);	
	ApiResponse<List<VerifyItemsResponse>> getTagsByProductName(String productName);
	ApiResponse<Void> deleteItem(Long id);
	ApiResponse<List<ItemDetailsResponse>> getLastRecentItems();
}
