package com.example.demo.timeblock.pattern.service;

import com.example.demo.timeblock.pattern.dto.PatternExceptionRequest;
import com.example.demo.timeblock.pattern.dto.PatternRequest;
import com.example.demo.timeblock.pattern.dto.PatternResponse;
import com.example.demo.timeblock.pattern.repository.RepeatPatternExceptionRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class PatternServiceTest {

	@Autowired
	private PatternService patternService;

	@Autowired
	private RepeatPatternExceptionRepository exceptionRepository;

	private final String userId = "pattern-user";

	@Test
	@DisplayName("creates repeat pattern and lists it")
	void createPattern() {
		PatternRequest request = new PatternRequest(
				"Weekday study",
				"#88B",
				"pencil",
				LocalTime.of(9, 0),
				LocalTime.of(11, 0),
				Set.of(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY),
				true,
				"Focus block"
		);

		PatternResponse response = patternService.createPattern(userId, request);
		assertThat(response.id()).isNotNull();
		assertThat(patternService.listPatterns(userId)).anyMatch(p -> p.id().equals(response.id()));
	}

	@Test
	@DisplayName("adds and removes exception dates")
	void manageException() {
		PatternRequest request = new PatternRequest(
				"Saturday gym",
				"#FF0",
				"dumbbell",
				LocalTime.of(7, 0),
				LocalTime.of(8, 0),
				Set.of(DayOfWeek.SATURDAY),
				true,
				null
		);
		PatternResponse response = patternService.createPattern(userId, request);
		LocalTime time = LocalTime.of(7, 0);
		java.time.LocalDate date = java.time.LocalDate.now().with(java.time.temporal.TemporalAdjusters.nextOrSame(DayOfWeek.SATURDAY));

		patternService.addException(userId, response.id(), new PatternExceptionRequest(date, "Cancel"));
		assertThat(exceptionRepository.existsByPatternAndDate(
				patternService.findOwnedPattern(userId, response.id()),
				date)).isTrue();

		patternService.removeException(userId, response.id(), date);
		assertThat(exceptionRepository.existsByPatternAndDate(
				patternService.findOwnedPattern(userId, response.id()),
				date)).isFalse();
	}
}
