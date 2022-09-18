package com.blog.app.exceptions;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.blog.app.payloads.ApiResponse;

@RestControllerAdvice
public class GlobalExceptionHander {

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ApiResponse> resourceNotFoundExceptionHandler(ResourceNotFoundException ex) {
		String message = ex.getMessage();
		ApiResponse response = new ApiResponse(message, false);
		return new ResponseEntity<ApiResponse>(response, HttpStatus.NOT_FOUND);

	}
	
	@ExceptionHandler(InvalidCredentialsException.class)
	public ResponseEntity<ApiResponse> invalidCredentialsExceptionHandler(InvalidCredentialsException ex) {
		String message = ex.getMessage();
		ApiResponse response = new ApiResponse(message, false);
		return new ResponseEntity<ApiResponse>(response, HttpStatus.UNAUTHORIZED);

	}


	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, String>> methodArgumentNotValidExceptionHandler(
			MethodArgumentNotValidException ex) {
		Map<String, String> errors = new HashMap<>();
		ex.getBindingResult().getAllErrors().forEach((error) -> {
			String fieldName = ((FieldError) error).getField();
			String errorMessage = error.getDefaultMessage();

			errors.put(fieldName, errorMessage);

		});

		return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
	}
}
