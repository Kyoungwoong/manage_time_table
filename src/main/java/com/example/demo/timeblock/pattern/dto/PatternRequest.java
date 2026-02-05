package com.example.demo.timeblock.pattern.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Set;

public record PatternRequest(
		@NotBlank String name,
		@NotBlank String color,
		@NotBlank String icon,
		@NotNull LocalTime startTime,
		@NotNull LocalTime endTime,
		@NotEmpty Set<DayOfWeek> weekdays,
		boolean isActive,
		String defaultNotes
) {
}
