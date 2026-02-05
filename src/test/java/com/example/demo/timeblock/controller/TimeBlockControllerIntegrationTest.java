package com.example.demo.timeblock.controller;

import com.example.demo.timeblock.dto.StatusUpdateRequest;
import com.example.demo.timeblock.dto.TimeBlockRequest;
import com.example.demo.timeblock.dto.TimeBlockResponse;
import com.example.demo.timeblock.dto.TimelineResponse;
import com.example.demo.timeblock.model.TimeBlockStatus;
import com.example.demo.timeblock.pattern.dto.PatternInstanceResponse;
import com.example.demo.timeblock.pattern.dto.PatternRequest;
import com.example.demo.timeblock.pattern.dto.PatternResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TimeBlockControllerIntegrationTest {

	private static final String USER_HEADER = "X-User-Id";

	@LocalServerPort
	private int port;

	private final RestTemplate restTemplate = new RestTemplate(
			new HttpComponentsClientHttpRequestFactory(HttpClients.createDefault())
	);

	@Test
	@DisplayName("creates block and returns timeline with recap")
	void createAndRetrieve() {
		LocalDate today = LocalDate.now();
		TimeBlockRequest request = new TimeBlockRequest(
				LocalTime.of(6, 0),
				LocalTime.of(7, 0),
				"Morning routine",
				"#AAA",
				"sun",
				null,
				null
		);

		HttpHeaders headers = new HttpHeaders();
		headers.set(USER_HEADER, "integration-user");
		HttpEntity<TimeBlockRequest> entity = new HttpEntity<>(request, headers);
		ResponseEntity<TimeBlockResponse> createResponse = restTemplate.exchange(
				uri("/api/timeline/" + today + "/blocks"),
				HttpMethod.POST,
				entity,
				new ParameterizedTypeReference<>() {}
		);

		assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
		assertThat(createResponse.getBody()).isNotNull();

		ResponseEntity<TimelineResponse> timeline = restTemplate.exchange(
				uri("/api/timeline/" + today),
				HttpMethod.GET,
				new HttpEntity<>(headers),
				new ParameterizedTypeReference<>() {}
		);

		assertThat(timeline.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(timeline.getBody()).isNotNull();
		assertThat(timeline.getBody().blocks()).hasSize(1);
		assertThat(timeline.getBody().recap().plannedMinutes()).isEqualTo(60);
	}

	@Test
	@DisplayName("updates status via API")
	void statusPatch() {
		LocalDate date = LocalDate.now();
		TimeBlockRequest request = new TimeBlockRequest(
				LocalTime.of(14, 0),
				LocalTime.of(15, 0),
				"Focus",
				"#123",
				"mind",
				null,
				null
		);

		HttpHeaders headers = new HttpHeaders();
		headers.set(USER_HEADER, "instr");
		HttpEntity<TimeBlockRequest> entity = new HttpEntity<>(request, headers);

		ResponseEntity<TimeBlockResponse> create = restTemplate.exchange(
				uri("/api/timeline/" + date + "/blocks"),
				HttpMethod.POST,
				entity,
				new ParameterizedTypeReference<>() {}
		);
		assertThat(create.getStatusCode()).isEqualTo(HttpStatus.CREATED);

		TimelineResponse timeline = restTemplate.exchange(
				uri("/api/timeline/" + date),
				HttpMethod.GET,
				new HttpEntity<>(headers),
				new ParameterizedTypeReference<TimelineResponse>() {}
		).getBody();

		assertThat(timeline).isNotNull();
		Long blockId = timeline.blocks().get(0).id();

		StatusUpdateRequest statusRequest = new StatusUpdateRequest(TimeBlockStatus.COMPLETED);
		ResponseEntity<TimeBlockResponse> patch = restTemplate.exchange(
				uri("/api/blocks/" + blockId + "/status"),
				HttpMethod.PATCH,
				new HttpEntity<>(statusRequest, headers),
				new ParameterizedTypeReference<>() {}
		);

		assertThat(patch.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(patch.getBody()).isNotNull();
	}

	@Test
	@DisplayName("previews pattern instances")
	void previewPatternInstances() {
		LocalDate date = LocalDate.now();
		PatternRequest request = new PatternRequest(
				"Morning loop",
				"#CCC",
				"sun",
				LocalTime.of(8, 0),
				LocalTime.of(9, 0),
				Set.of(date.getDayOfWeek()),
				true,
				"Pattern note"
		);

		HttpHeaders headers = new HttpHeaders();
		headers.set(USER_HEADER, "pattern-user");

		ResponseEntity<PatternResponse> created = restTemplate.exchange(
				uri("/api/patterns"),
				HttpMethod.POST,
				new HttpEntity<>(request, headers),
				PatternResponse.class
		);

		assertThat(created.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(created.getBody()).isNotNull();

		ResponseEntity<PatternInstanceResponse[]> preview = restTemplate.exchange(
				uri("/api/patterns/" + created.getBody().id() + "/instances?date=" + date),
				HttpMethod.GET,
				new HttpEntity<>(headers),
				PatternInstanceResponse[].class
		);

		assertThat(preview.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(preview.getBody()).isNotNull();
		assertThat(preview.getBody()).hasSize(1);
		assertThat(preview.getBody()[0].isException()).isFalse();
	}

	private String uri(String path) {
		return "http://localhost:" + port + path;
	}
}
