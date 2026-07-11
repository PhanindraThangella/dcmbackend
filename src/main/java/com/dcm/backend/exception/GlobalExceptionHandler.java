package com.dcm.backend.exception;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.dcm.backend.dto.response.ApiResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ApiResponse<Void>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex)
	{
		String errorMsg = ex.getBindingResult().getFieldErrors().stream()
			    .map(error -> error.getField() + ": " + error.getDefaultMessage())
			    .collect(Collectors.joining(", "));
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(ApiResponse.<Void>builder()
				.success(false)
				.message(errorMsg)
				.timestamp(LocalDateTime.now())
				.build());
	}
    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ResponseEntity<ApiResponse<Void>> handleResourceAlreadyExists(
            ResourceAlreadyExistsException ex) {

        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ApiResponse.<Void>builder()
                        .success(false)
                        .message(ex.getMessage())
                        .timestamp(LocalDateTime.now())
                        .build());
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleResourceNotFound(
            ResourceNotFoundException ex) {

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.<Void>builder()
                        .success(false)
                        .message(ex.getMessage())
                        .timestamp(LocalDateTime.now())
                        .build());
    }

    @ExceptionHandler({
            AuthenticationFailedException.class,
            BadCredentialsException.class
    })
    public ResponseEntity<ApiResponse<Void>> handleAuthentication(Exception ex) {

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ApiResponse.<Void>builder()
                        .success(false)
                        .message("Invalid username or password.")
                        .timestamp(LocalDateTime.now())
                        .build());
    }

//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public ResponseEntity<ApiResponse<Map<String, String>>> handleValidation(
//            MethodArgumentNotValidException ex) {
//
//        Map<String, String> errors = new HashMap<>();
//
//        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
//            errors.put(error.getField(), error.getDefaultMessage());
//        }
//
//        return ResponseEntity.badRequest()
//                .body(ApiResponse.<Map<String, String>>builder()
//                        .success(false)
//                        .message("Validation failed.")
//                        .data(errors)
//                        .timestamp(LocalDateTime.now())
//                        .build());
//    }
    @ExceptionHandler(TransactionsNotFound.class)
    public ResponseEntity<ApiResponse<Void>> handleTransactionsNotFound(
            ResourceNotFoundException ex) {

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.<Void>builder()
                        .success(false)
                        .message(ex.getMessage())
                        .timestamp(LocalDateTime.now())
                        .build());
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleException(Exception ex) {

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.<Void>builder()
                        .success(false)
                        .message("An unexpected error occurred.")
                        .timestamp(LocalDateTime.now())
                        .build());
    }
}