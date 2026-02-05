package com.example.demo.timeblock.service;

import com.example.demo.timeblock.dto.TimeBlockRequest;
import com.example.demo.timeblock.dto.TimeBlockResponse;
import com.example.demo.timeblock.exception.TimeBlockConflictException;
import com.example.demo.timeblock.model.TimeBlockStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class TimeBlockServiceTest {

	@Autowired
	private TimeBlockService service;

	@Test
	@DisplayName("creates a block and defaults to PLANNED")
	void createTimeBlock() {
		TimeBlockRequest request = new TimeBlockRequest(
				LocalTime.of(9, 0),
				LocalTime.of(10, 0),
				"Study",
				"#88B",
				"book",
				null,
				"Focus block"
		);

		LocalDate date = LocalDate.now();
		TimeBlockResponse response = service.createBlock("user-a", date, request);

		assertThat(response.status()).isEqualTo(TimeBlockStatus.PLANNED);
		assertThat(response.durationMinutes()).isEqualTo(60);
		assertThat(response.notes()).isEqualTo("Focus block");
	}

	@Test
	@DisplayName("rejects overlapping blocks")
	void overlappingBlocks() {
		TimeBlockRequest first = new TimeBlockRequest(
				LocalTime.of(8, 0),
				LocalTime.of(9, 0),
				"Math",
				"#A11",
				"pencil",
				null,
				null
		);
		service.createBlock("user-b", LocalDate.now(), first);

		TimeBlockRequest conflict = new TimeBlockRequest(
				LocalTime.of(8, 30),
				LocalTime.of(9, 30),
				"English",
				"#B22",
				"pen",
				null,
				null
		);

		assertThatThrownBy(() -> service.createBlock("user-b", LocalDate.now(), conflict))
				.isInstanceOf(TimeBlockConflictException.class);
	}

	@Test
	@DisplayName("updates block metadata")
	void updateBlock() {
		TimeBlockRequest initial = new TimeBlockRequest(
				LocalTime.of(7, 0),
				LocalTime.of(8, 0),
				"Pre-run",
				"#123",
				"air",
				null,
				null
		);
		TimeBlockResponse created = service.createBlock("user-c", LocalDate.now(), initial);

		TimeBlockRequest updated = new TimeBlockRequest(
				LocalTime.of(7, 30),
				LocalTime.of(8, 30),
				"Warm-up",
				"#456",
				"fire",
				42L,
				"Updated notes"
		);

		TimeBlockResponse response = service.updateBlock("user-c", created.id(), updated);
		assertThat(response.title()).isEqualTo("Warm-up");
		assertThat(response.patternId()).isEqualTo(42L);
	}

	@Test
	@DisplayName("handles status transitions")
	void statusTransition() {
		TimeBlockRequest request = new TimeBlockRequest(
				LocalTime.of(10, 0),
				LocalTime.of(11, 0),
				"Deep work",
				"#111",
				"lab",
				null,
				null
		);
		TimeBlockResponse created = service.createBlock("user-d", LocalDate.now(), request);
		TimeBlockResponse partial = service.updateStatus("user-d", created.id(), TimeBlockStatus.PARTIAL);
		assertThat(partial.status()).isEqualTo(TimeBlockStatus.PARTIAL);
		TimeBlockResponse completed = service.updateStatus("user-d", created.id(), TimeBlockStatus.COMPLETED);
		assertThat(completed.status()).isEqualTo(TimeBlockStatus.COMPLETED);
	}

	@Test
	@DisplayName("denies reverting to planned via status patch")
	void revertToPlanned() {
		TimeBlockRequest request = new TimeBlockRequest(
				LocalTime.of(12, 0),
				LocalTime.of(13, 0),
				"Stretch",
				"#999",
				"body",
				null,
				null
		);
		TimeBlockResponse block = service.createBlock("user-e", LocalDate.now(), request);
		service.updateStatus("user-e", block.id(), TimeBlockStatus.MISSED);

		assertThatThrownBy(() -> service.updateStatus("user-e", block.id(), TimeBlockStatus.PLANNED))
				.isInstanceOf(IllegalArgumentException.class);
	}
}
