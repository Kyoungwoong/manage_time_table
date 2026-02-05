package com.example.demo.timeblock.pattern.service;

import com.example.demo.timeblock.pattern.dto.PatternInstanceResponse;
import com.example.demo.timeblock.pattern.entity.RepeatPattern;
import com.example.demo.timeblock.pattern.entity.RepeatPatternException;
import com.example.demo.timeblock.pattern.repository.RepeatPatternExceptionRepository;
import com.example.demo.timeblock.pattern.repository.RepeatPatternRepository;
import com.example.demo.timeblock.repository.TimeBlockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RepeatExpansionService {

	private final RepeatPatternRepository patternRepository;
	private final RepeatPatternExceptionRepository exceptionRepository;
	private final TimeBlockRepository timeBlockRepository;

	@Transactional(readOnly = true)
	public List<PatternInstanceResponse> previewForDate(String userId, LocalDate date) {
		DayOfWeek target = date.getDayOfWeek();
		return patternRepository.findAllByUserIdAndIsActiveTrue(userId).stream()
				.filter(pattern -> pattern.getWeekdays().contains(target))
				.map(pattern -> buildInstance(userId, date, pattern))
				.toList();
	}

	@Transactional(readOnly = true)
	public List<PatternInstanceResponse> previewPattern(String userId, Long patternId, LocalDate date) {
		RepeatPattern pattern = patternRepository.findByIdAndUserId(patternId, userId)
				.orElseThrow(() -> new IllegalArgumentException("Pattern not found"));
		if (!pattern.isActive()) {
			return List.of();
		}
		if (!pattern.getWeekdays().contains(date.getDayOfWeek())) {
			return List.of();
		}
		return List.of(buildInstance(userId, date, pattern));
	}

	private PatternInstanceResponse buildInstance(String userId, LocalDate date, RepeatPattern pattern) {
		Optional<RepeatPatternException> exception = exceptionRepository.findByPatternAndDate(pattern, date);
		boolean conflict = timeBlockRepository.existsConflictingBlock(userId, date, pattern.getStartTime(), pattern.getEndTime(), null);
		boolean isException = exception.isPresent();
		return new PatternInstanceResponse(
				pattern.getId(),
				pattern.getName(),
				pattern.getColor(),
				pattern.getIcon(),
				pattern.getStartTime(),
				pattern.getEndTime(),
				isException,
				exception.map(RepeatPatternException::getReason).orElse(null),
				conflict && !isException
		);
	}
}
