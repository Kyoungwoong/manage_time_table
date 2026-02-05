package com.example.demo.timeblock.service;

import com.example.demo.timeblock.dto.*;
import com.example.demo.timeblock.entity.TimeBlock;
import com.example.demo.timeblock.exception.TimeBlockConflictException;
import com.example.demo.timeblock.model.TimeBlockStatus;
import com.example.demo.timeblock.repository.TimeBlockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TimeBlockService {

	private final TimeBlockRepository repository;

	@Transactional
	public TimeBlockResponse createBlock(String userId, LocalDate date, TimeBlockRequest request) {
		validateWindow(request.startTime(), request.endTime());
		validateConflict(userId, date, request.startTime(), request.endTime(), null);
		TimeBlock block = TimeBlock.builder()
				.userId(userId)
				.date(date)
				.startTime(request.startTime())
				.endTime(request.endTime())
				.title(request.title())
				.color(request.color())
				.icon(request.icon())
				.notes(request.notes())
				.patternId(request.patternId())
				.status(TimeBlockStatus.PLANNED)
				.build();
		return TimeBlockResponse.fromEntity(repository.save(block));
	}

	@Transactional(readOnly = true)
	public TimelineResponse getTimeline(String userId, LocalDate date) {
		List<TimeBlock> blocks = repository.findAllByUserIdAndDateOrderByStartTimeAsc(userId, date);

		List<TimeBlockResponse> responses = blocks.stream()
				.map(TimeBlockResponse::fromEntity)
				.collect(Collectors.toList());

		RecapMetrics recap = computeRecap(blocks);
		return new TimelineResponse(responses, recap);
	}

	@Transactional
	public TimeBlockResponse updateBlock(String userId, Long blockId, TimeBlockRequest request) {
		validateWindow(request.startTime(), request.endTime());
		TimeBlock block = findOwnedBlock(userId, blockId);
		validateConflict(userId, block.getDate(), request.startTime(), request.endTime(), blockId);
		block.setStartTime(request.startTime());
		block.setEndTime(request.endTime());
		block.setTitle(request.title());
		block.setColor(request.color());
		block.setIcon(request.icon());
		block.setNotes(request.notes());
		block.setPatternId(request.patternId());
		return TimeBlockResponse.fromEntity(repository.save(block));
	}

	@Transactional
	public TimeBlockResponse updateStatus(String userId, Long blockId, TimeBlockStatus newStatus) {
		TimeBlock block = findOwnedBlock(userId, blockId);
		if (newStatus == TimeBlockStatus.PLANNED && block.getStatus() != TimeBlockStatus.PLANNED) {
			throw new IllegalArgumentException("Reverting to PLANNED is only allowed via block update.");
		}
		if (block.getStatus() == TimeBlockStatus.PLANNED && newStatus == TimeBlockStatus.PLANNED) {
			return TimeBlockResponse.fromEntity(block);
		}
		block.setStatus(newStatus);
		return TimeBlockResponse.fromEntity(repository.save(block));
	}

	@Transactional
	public void deleteBlock(String userId, Long blockId) {
		TimeBlock block = findOwnedBlock(userId, blockId);
		repository.delete(block);
	}

	private TimeBlock findOwnedBlock(String userId, Long blockId) {
		return repository.findById(blockId)
				.filter(b -> b.getUserId().equals(userId))
				.orElseThrow(() -> new IllegalArgumentException("Block not found or access denied."));
	}

	private void validateWindow(LocalTime start, LocalTime end) {
		if (!start.isBefore(end)) {
			throw new IllegalArgumentException("startTime must be before endTime.");
		}
	}

	private void validateConflict(String userId, LocalDate date, LocalTime start, LocalTime end, Long blockId) {
		boolean conflict = repository.existsConflictingBlock(userId, date, start, end, blockId);
		if (conflict) {
			throw new TimeBlockConflictException("Time block conflicts with existing block.");
		}
	}

	private RecapMetrics computeRecap(List<TimeBlock> blocks) {
		long plannedMinutes = blocks.stream()
				.mapToLong(TimeBlock::durationMinutes)
				.sum();

		long actualMinutes = blocks.stream()
				.mapToLong(block -> switch (block.getStatus()) {
					case COMPLETED -> block.durationMinutes();
					case PARTIAL -> block.durationMinutes() / 2;
					default -> 0L;
				})
				.sum();

		long partialCount = blocks.stream()
				.filter(block -> block.getStatus() == TimeBlockStatus.PARTIAL)
				.count();

		long missedCount = blocks.stream()
				.filter(block -> block.getStatus() == TimeBlockStatus.MISSED)
				.count();

		return new RecapMetrics(plannedMinutes, actualMinutes, partialCount, missedCount);
	}
}
