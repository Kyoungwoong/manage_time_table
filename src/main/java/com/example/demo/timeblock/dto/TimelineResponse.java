package com.example.demo.timeblock.dto;

import java.util.List;

public record TimelineResponse(List<TimeBlockResponse> blocks, RecapMetrics recap) {
}
