package com.example.demo.timeblock.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(TimeBlockConflictException.class)
	public ResponseEntity<ApiErrorResponse> handleTimeBlockConflict(TimeBlockConflictException ex, HttpServletRequest request) {
		HttpStatus status = HttpStatus.CONFLICT;
		return ResponseEntity.status(status).body(new ApiErrorResponse(
				OffsetDateTime.now(ZoneOffset.UTC),
				status.value(),
				status.getReasonPhrase(),
				ErrorCode.TIMEBLOCK_CONFLICT.name(),
				ex.getMessage(),
				request.getRequestURI(),
				null
		));
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ApiErrorResponse> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpServletRequest request) {
		HttpStatus status = HttpStatus.BAD_REQUEST;
		List<ApiErrorDetail> details = ex.getBindingResult().getFieldErrors().stream()
				.map(err -> new ApiErrorDetail(err.getField(), err.getDefaultMessage()))
				.toList();

		return ResponseEntity.status(status).body(new ApiErrorResponse(
				OffsetDateTime.now(ZoneOffset.UTC),
				status.value(),
				status.getReasonPhrase(),
				ErrorCode.VALIDATION_ERROR.name(),
				"Validation failed.",
				request.getRequestURI(),
				details
		));
	}

	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<ApiErrorResponse> handleConstraintViolation(ConstraintViolationException ex, HttpServletRequest request) {
		HttpStatus status = HttpStatus.BAD_REQUEST;
		List<ApiErrorDetail> details = ex.getConstraintViolations().stream()
				.map(v -> new ApiErrorDetail(v.getPropertyPath().toString(), v.getMessage()))
				.toList();

		return ResponseEntity.status(status).body(new ApiErrorResponse(
				OffsetDateTime.now(ZoneOffset.UTC),
				status.value(),
				status.getReasonPhrase(),
				ErrorCode.VALIDATION_ERROR.name(),
				"Validation failed.",
				request.getRequestURI(),
				details
		));
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<ApiErrorResponse> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpServletRequest request) {
		HttpStatus status = HttpStatus.BAD_REQUEST;
		return ResponseEntity.status(status).body(new ApiErrorResponse(
				OffsetDateTime.now(ZoneOffset.UTC),
				status.value(),
				status.getReasonPhrase(),
				ErrorCode.MALFORMED_JSON.name(),
				"Malformed JSON request.",
				request.getRequestURI(),
				null
		));
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<ApiErrorResponse> handleIllegalArgument(IllegalArgumentException ex, HttpServletRequest request) {
		String message = ex.getMessage() != null ? ex.getMessage() : "Invalid request.";
		String normalized = message.toLowerCase();

		boolean notFoundOrDenied = normalized.contains("not found") || normalized.contains("access denied");
		HttpStatus status = notFoundOrDenied ? HttpStatus.NOT_FOUND : HttpStatus.BAD_REQUEST;
		ErrorCode code = notFoundOrDenied ? ErrorCode.RESOURCE_NOT_FOUND : ErrorCode.VALIDATION_ERROR;

		return ResponseEntity.status(status).body(new ApiErrorResponse(
				OffsetDateTime.now(ZoneOffset.UTC),
				status.value(),
				status.getReasonPhrase(),
				code.name(),
				message,
				request.getRequestURI(),
				null
		));
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ApiErrorResponse> handleUnexpected(Exception ex, HttpServletRequest request) {
		HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
		return ResponseEntity.status(status).body(new ApiErrorResponse(
				OffsetDateTime.now(ZoneOffset.UTC),
				status.value(),
				status.getReasonPhrase(),
				ErrorCode.INTERNAL_ERROR.name(),
				"Internal server error.",
				request.getRequestURI(),
				null
		));
	}
}
