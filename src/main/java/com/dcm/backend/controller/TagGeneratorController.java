package com.dcm.backend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dcm.backend.dto.request.TagGenerateRequest;
import com.dcm.backend.dto.response.ApiResponse;
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
}
