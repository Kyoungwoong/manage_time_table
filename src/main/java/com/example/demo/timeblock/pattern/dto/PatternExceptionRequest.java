package com.example.demo.timeblock.pattern.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record PatternExceptionRequest(
		@NotNull LocalDate date,
		String reason
) {
}
