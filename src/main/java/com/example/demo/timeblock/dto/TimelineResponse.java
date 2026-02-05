package com.example.demo.timeblock.dto;

import com.example.demo.timeblock.pattern.dto.PatternInstanceResponse;

import java.util.List;

public record TimelineResponse(List<TimeBlockResponse> blocks, RecapMetrics recap, List<PatternInstanceResponse> patternInstances) {
}
