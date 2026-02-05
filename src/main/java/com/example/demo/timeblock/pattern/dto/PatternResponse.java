package com.example.demo.timeblock.pattern.dto;

import com.example.demo.timeblock.pattern.entity.RepeatPattern;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Set;

public record PatternResponse(
		Long id,
		String name,
		String color,
		String icon,
		LocalTime startTime,
		LocalTime endTime,
		Set<DayOfWeek> weekdays,
		boolean isActive,
		String defaultNotes
) {
	public static PatternResponse fromEntity(RepeatPattern pattern) {
		return new PatternResponse(
				pattern.getId(),
				pattern.getName(),
				pattern.getColor(),
				pattern.getIcon(),
				pattern.getStartTime(),
				pattern.getEndTime(),
				pattern.getWeekdays(),
				pattern.isActive(),
				pattern.getDefaultNotes()
		);
	}
}
