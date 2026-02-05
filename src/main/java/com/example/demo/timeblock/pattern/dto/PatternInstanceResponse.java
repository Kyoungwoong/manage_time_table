package com.example.demo.timeblock.pattern.dto;

import java.time.LocalTime;

public record PatternInstanceResponse(
		Long patternId,
		String name,
		String color,
		String icon,
		LocalTime startTime,
		LocalTime endTime,
		boolean isException,
		String exceptionReason,
		boolean hasConflict
) {
}
