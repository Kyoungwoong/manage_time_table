package com.example.demo.timeblock.pattern.service;

import com.example.demo.timeblock.pattern.dto.PatternExceptionRequest;
import com.example.demo.timeblock.pattern.dto.PatternRequest;
import com.example.demo.timeblock.pattern.dto.PatternResponse;
import com.example.demo.timeblock.pattern.entity.RepeatPattern;
import com.example.demo.timeblock.pattern.entity.RepeatPatternException;
import com.example.demo.timeblock.pattern.repository.RepeatPatternExceptionRepository;
import com.example.demo.timeblock.pattern.repository.RepeatPatternRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PatternService {

	private final RepeatPatternRepository patternRepository;
	private final RepeatPatternExceptionRepository exceptionRepository;

	@Transactional
	public PatternResponse createPattern(String userId, PatternRequest request) {
		RepeatPattern pattern = RepeatPattern.builder()
				.userId(userId)
				.name(request.name())
				.color(request.color())
				.icon(request.icon())
				.startTime(request.startTime())
				.endTime(request.endTime())
				.weekdays(request.weekdays())
				.isActive(request.isActive())
				.defaultNotes(request.defaultNotes())
				.build();
		return PatternResponse.fromEntity(patternRepository.save(pattern));
	}

	@Transactional(readOnly = true)
	public List<PatternResponse> listPatterns(String userId) {
		return patternRepository.findAllByUserIdAndIsActiveTrue(userId).stream()
				.map(PatternResponse::fromEntity)
				.toList();
	}

	@Transactional
	public void addException(String userId, Long patternId, PatternExceptionRequest request) {
		RepeatPattern pattern = findOwnedPattern(userId, patternId);
		if (exceptionRepository.existsByPatternAndDate(pattern, request.date())) {
			return;
		}
		RepeatPatternException exception = RepeatPatternException.builder()
				.pattern(pattern)
				.date(request.date())
				.reason(request.reason())
				.build();
		exceptionRepository.save(exception);
	}

	@Transactional
	public void removeException(String userId, Long patternId, java.time.LocalDate date) {
		RepeatPattern pattern = findOwnedPattern(userId, patternId);
		RepeatPatternException exception = exceptionRepository.findByPatternAndDate(pattern, date)
				.orElseThrow(() -> new IllegalArgumentException("Exception not found"));
		exceptionRepository.delete(exception);
	}

	@Transactional(readOnly = true)
	public RepeatPattern findOwnedPattern(String userId, Long patternId) {
		return patternRepository.findByIdAndUserId(patternId, userId)
				.orElseThrow(() -> new IllegalArgumentException("Pattern not found"));
	}
}
