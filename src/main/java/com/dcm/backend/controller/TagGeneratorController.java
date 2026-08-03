package com.dcm.backend.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dcm.backend.dto.request.TagGenerateRequest;
import com.dcm.backend.dto.response.ApiResponse;
import com.dcm.backend.dto.response.ItemDetailsResponse;
import com.dcm.backend.dto.response.VerifyItemsResponse;
import com.dcm.backend.service.ItemTagRelatedService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/tags")
@RequiredArgsConstructor
@Validated
public class TagGeneratorController {
	private final ItemTagRelatedService itemService;
	@PostMapping("/createNewTag")
	public ResponseEntity<ApiResponse<Void>> createNewTagCotroller(@RequestBody TagGenerateRequest request)
	{
		ApiResponse<Void> response=this.itemService.createNewTag(request);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
	@PutMapping("/updateTagDetails")
	public ResponseEntity<ApiResponse<Void>> updateTagDetails(@RequestBody TagGenerateRequest request)
	{
		ApiResponse<Void> response=this.itemService.UpdateTagDetails(request);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
	@GetMapping("/getTagDetails/{tagNumber}")
	public ResponseEntity<ApiResponse<ItemDetailsResponse>> getTagDetails(@PathVariable("tagNumber") Long TagNumber)
	{
		ApiResponse<ItemDetailsResponse> response=this.itemService.fetchTagDetails(TagNumber);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
	@GetMapping("/getItems/{productName}")
	public ResponseEntity<ApiResponse<List<VerifyItemsResponse>>> getTagDetails(@PathVariable("productName") String productName)
	{
		ApiResponse<List<VerifyItemsResponse>> response=this.itemService.getTagsByProductName(productName);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
	@DeleteMapping("/deleteItem/{id}")
	public ResponseEntity<ApiResponse<Void>> deleteItem(@PathVariable("id")Long id)
	{
		ApiResponse<Void>response=this.itemService.deleteItem(id);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
	@GetMapping("/getRecentItems")
	public ResponseEntity<ApiResponse<List<ItemDetailsResponse>>> getRecentTags()
	{
		ApiResponse<List<ItemDetailsResponse>> response=this.itemService.getLastRecentItems();
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
}

