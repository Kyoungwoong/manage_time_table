package com.example.demo.timeblock.pattern.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.OffsetDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "repeat_pattern_exception")
public class RepeatPatternException {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(optional = false)
	@JoinColumn(name = "pattern_id", nullable = false)
	private RepeatPattern pattern;

	@Column(nullable = false)
	private LocalDate date;

	@Column(length = 400)
	private String reason;

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
