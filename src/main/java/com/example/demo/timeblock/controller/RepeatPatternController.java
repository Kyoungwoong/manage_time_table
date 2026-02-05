package com.example.demo.timeblock.controller;

import com.example.demo.timeblock.pattern.dto.PatternExceptionRequest;
import com.example.demo.timeblock.pattern.dto.PatternInstanceResponse;
import com.example.demo.timeblock.pattern.dto.PatternRequest;
import com.example.demo.timeblock.pattern.dto.PatternResponse;
import com.example.demo.timeblock.pattern.service.PatternService;
import com.example.demo.timeblock.pattern.service.RepeatExpansionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/patterns")
@Validated
@RequiredArgsConstructor
public class RepeatPatternController {

	private static final String DEFAULT_USER = "demo-user";
	private final PatternService patternService;
	private final RepeatExpansionService expansionService;

	@PostMapping
	public ResponseEntity<PatternResponse> createPattern(
			@RequestHeader(value = "X-User-Id", required = false) String requester,
			@Valid @RequestBody PatternRequest request) {
		String userId = resolveUser(requester);
		return ResponseEntity.ok(patternService.createPattern(userId, request));
	}

	@GetMapping
	public ResponseEntity<List<PatternResponse>> listPatterns(
			@RequestHeader(value = "X-User-Id", required = false) String requester) {
		String userId = resolveUser(requester);
		return ResponseEntity.ok(patternService.listPatterns(userId));
	}

	@PostMapping("/{id}/exceptions")
	public ResponseEntity<Void> addException(
			@RequestHeader(value = "X-User-Id", required = false) String requester,
			@PathVariable Long id,
			@Valid @RequestBody PatternExceptionRequest request) {
		String userId = resolveUser(requester);
		patternService.addException(userId, id, request);
		return ResponseEntity.noContent().build();
	}

	@DeleteMapping("/{id}/exceptions/{date}")
	public ResponseEntity<Void> removeException(
			@RequestHeader(value = "X-User-Id", required = false) String requester,
			@PathVariable Long id,
			@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
		String userId = resolveUser(requester);
		patternService.removeException(userId, id, date);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/{id}/instances")
	public ResponseEntity<List<PatternInstanceResponse>> previewPattern(
			@RequestHeader(value = "X-User-Id", required = false) String requester,
			@PathVariable Long id,
			@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
		String userId = resolveUser(requester);
		return ResponseEntity.ok(expansionService.previewPattern(userId, id, date));
	}

	private String resolveUser(String header) {
		return header != null && !header.isBlank() ? header : DEFAULT_USER;
	}
}
