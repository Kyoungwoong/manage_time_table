package com.example.demo.timeblock.dto;

import com.example.demo.timeblock.model.TimeBlockStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalTime;

public record TimeBlockRequest(
		@NotNull LocalTime startTime,
		@NotNull LocalTime endTime,
		@NotBlank String title,
		@NotBlank String color,
		@NotBlank String icon,
		Long patternId,
		String notes
) {
}
