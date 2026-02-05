package com.example.demo.timeblock.dto;

import com.example.demo.timeblock.model.TimeBlockStatus;
import jakarta.validation.constraints.NotNull;

public record StatusUpdateRequest(@NotNull TimeBlockStatus status) {
}
