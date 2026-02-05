package com.example.demo.timeblock.controller;

import com.example.demo.timeblock.dto.*;
import com.example.demo.timeblock.model.TimeBlockStatus;
import com.example.demo.timeblock.service.TimeBlockService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@Validated
@RequiredArgsConstructor
public class TimeBlockController {

	private static final String DEFAULT_USER = "demo-user";
	private final TimeBlockService timeBlockService;

	@PostMapping("/api/timeline/{date}/blocks")
	public ResponseEntity<TimeBlockResponse> createBlock(
			@RequestHeader(value = "X-User-Id", required = false) String requester,
			@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
			@Valid @RequestBody TimeBlockRequest request) {
		String userId = resolveUserId(requester);
		TimeBlockResponse response = timeBlockService.createBlock(userId, date, request);
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@GetMapping("/api/timeline/{date}")
	public ResponseEntity<TimelineResponse> getTimeline(
			@RequestHeader(value = "X-User-Id", required = false) String requester,
			@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
		String userId = resolveUserId(requester);
		return ResponseEntity.ok(timeBlockService.getTimeline(userId, date));
	}

	@PutMapping("/api/blocks/{id}")
	public ResponseEntity<TimeBlockResponse> updateBlock(
			@RequestHeader(value = "X-User-Id", required = false) String requester,
			@PathVariable Long id,
			@Valid @RequestBody TimeBlockRequest request) {
		String userId = resolveUserId(requester);
		return ResponseEntity.ok(timeBlockService.updateBlock(userId, id, request));
	}

	@PatchMapping("/api/blocks/{id}/status")
	public ResponseEntity<TimeBlockResponse> patchStatus(
			@RequestHeader(value = "X-User-Id", required = false) String requester,
			@PathVariable Long id,
			@Valid @RequestBody StatusUpdateRequest request) {
		String userId = resolveUserId(requester);
		return ResponseEntity.ok(timeBlockService.updateStatus(userId, id, request.status()));
	}

	@DeleteMapping("/api/blocks/{id}")
	public ResponseEntity<Void> deleteBlock(
			@RequestHeader(value = "X-User-Id", required = false) String requester,
			@PathVariable Long id) {
		String userId = resolveUserId(requester);
		timeBlockService.deleteBlock(userId, id);
		return ResponseEntity.noContent().build();
	}

	private String resolveUserId(String header) {
		return header != null && !header.isBlank() ? header : DEFAULT_USER;
	}
}
