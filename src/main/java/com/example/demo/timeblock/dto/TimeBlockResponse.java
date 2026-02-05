package com.example.demo.timeblock.dto;

import com.example.demo.timeblock.entity.TimeBlock;
import com.example.demo.timeblock.model.TimeBlockStatus;

import java.time.LocalDate;
import java.time.LocalTime;

public record TimeBlockResponse(
		Long id,
		String userId,
		LocalDate date,
		LocalTime startTime,
		LocalTime endTime,
		long durationMinutes,
		String title,
		String color,
		String icon,
		TimeBlockStatus status,
		Long patternId,
		String notes
) {
	public static TimeBlockResponse fromEntity(TimeBlock block) {
		return new TimeBlockResponse(
				block.getId(),
				block.getUserId(),
				block.getDate(),
				block.getStartTime(),
				block.getEndTime(),
				block.durationMinutes(),
				block.getTitle(),
				block.getColor(),
				block.getIcon(),
				block.getStatus(),
				block.getPatternId(),
				block.getNotes()
		);
	}
}
