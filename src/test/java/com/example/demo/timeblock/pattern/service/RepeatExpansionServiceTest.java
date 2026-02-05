package com.example.demo.timeblock.pattern.service;

import com.example.demo.timeblock.dto.TimeBlockRequest;
import com.example.demo.timeblock.pattern.dto.PatternExceptionRequest;
import com.example.demo.timeblock.pattern.dto.PatternInstanceResponse;
import com.example.demo.timeblock.pattern.dto.PatternRequest;
import com.example.demo.timeblock.pattern.dto.PatternResponse;
import com.example.demo.timeblock.service.TimeBlockService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class RepeatExpansionServiceTest {

	@Autowired
	private RepeatExpansionService expansionService;

	@Autowired
	private PatternService patternService;

	@Autowired
	private TimeBlockService timeBlockService;

	private final String userId = "repeat-user";
	private PatternResponse pattern;
	private LocalDate targetDate;

	@BeforeEach
	void setUp() {
		targetDate = LocalDate.now();
		PatternRequest request = new PatternRequest(
				"Evening focus",
				"#456",
				"moon",
				LocalTime.of(20, 0),
				LocalTime.of(21, 0),
				Set.of(targetDate.getDayOfWeek()),
				true,
				"Study block"
		);
		pattern = patternService.createPattern(userId, request);
	}

	@Test
	@DisplayName("previews generation for date")
	void previewGeneratesPattern() {
		List<PatternInstanceResponse> instances = expansionService.previewPattern(userId, pattern.id(), targetDate);
		assertThat(instances).hasSize(1);
		assertThat(instances.get(0).isException()).isFalse();
		assertThat(instances.get(0).hasConflict()).isFalse();
	}

	@Test
	@DisplayName("marks exception dates")
	void previewSkipsExceptionDate() {
		patternService.addException(userId, pattern.id(), new PatternExceptionRequest(targetDate, "Rest"));
		List<PatternInstanceResponse> instances = expansionService.previewPattern(userId, pattern.id(), targetDate);
		assertThat(instances).hasSize(1);
		assertThat(instances.get(0).isException()).isTrue();
	}

	@Test
	@DisplayName("flags conflicts with manual blocks")
	void previewDetectsConflicts() {
		TimeBlockRequest manual = new TimeBlockRequest(
				LocalTime.of(20, 0),
				LocalTime.of(21, 0),
				"Manual",
				"#111",
				"note",
				null,
				null
		);
		timeBlockService.createBlock(userId, targetDate, manual);
		List<PatternInstanceResponse> instances = expansionService.previewPattern(userId, pattern.id(), targetDate);
		assertThat(instances).hasSize(1);
		assertThat(instances.get(0).hasConflict()).isTrue();
	}
}
