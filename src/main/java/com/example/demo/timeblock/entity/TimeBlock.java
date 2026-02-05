package com.example.demo.timeblock.entity;

import com.example.demo.timeblock.model.TimeBlockStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "time_block")
public class TimeBlock {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String userId;

	@Column(nullable = false)
	private LocalDate date;

	@Column(nullable = false)
	private LocalTime startTime;

	@Column(nullable = false)
	private LocalTime endTime;

	@Column(nullable = false)
	private String title;

	@Column(nullable = false)
	private String color;

	@Column(nullable = false)
	private String icon;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private TimeBlockStatus status;

	private Long patternId;

	@Column(length = 2000)
	private String notes;

	@Column(nullable = false, updatable = false)
	private OffsetDateTime createdAt;

	@Column(nullable = false)
	private OffsetDateTime updatedAt;

	public long durationMinutes() {
		return java.time.Duration.between(startTime, endTime).toMinutes();
	}

	@PrePersist
	void prePersist() {
		OffsetDateTime now = OffsetDateTime.now();
		createdAt = now;
		updatedAt = now;
	}

	@PreUpdate
	void preUpdate() {
		updatedAt = OffsetDateTime.now();
	}
}
