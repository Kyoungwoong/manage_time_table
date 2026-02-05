package com.example.demo.timeblock.dto;

public record RecapMetrics(long plannedMinutes, long actualMinutes, long partialCount, long missedCount) {
}
