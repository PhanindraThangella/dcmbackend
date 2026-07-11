package com.dcm.backend.service;

import com.dcm.backend.dto.request.TagGenerateRequest;
import com.dcm.backend.dto.response.ApiResponse;

public interface ItemTagRelatedService {
	ApiResponse<Void> createNewTag(TagGenerateRequest  request);
}
