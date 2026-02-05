package com.example.demo.timeblock.pattern.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "repeat_pattern")
public class RepeatPattern {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String userId;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false)
	private String color;

	@Column(nullable = false)
	private String icon;

	@Column(nullable = false)
	private LocalTime startTime;

	@Column(nullable = false)
	private LocalTime endTime;

	@Column(nullable = false)
	private boolean isActive;

	@Column(length = 2000)
	private String defaultNotes;

	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "repeat_pattern_weekdays", joinColumns = @JoinColumn(name = "pattern_id"))
	@Column(name = "weekday", nullable = false)
	@Enumerated(EnumType.STRING)
	@Builder.Default
	private Set<DayOfWeek> weekdays = new HashSet<>();

	@Column(nullable = false, updatable = false)
	private OffsetDateTime createdAt;

	@Column(nullable = false)
	private OffsetDateTime updatedAt;

	@PrePersist
	void prePersist() {
		OffsetDateTime now = OffsetDateTime.now();
		createdAt = now;
		updatedAt = now;
	}

	@PreUpdate
	protected void preUpdate() {
		updatedAt = OffsetDateTime.now();
	}
}
