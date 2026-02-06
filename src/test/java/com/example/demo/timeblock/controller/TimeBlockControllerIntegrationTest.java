package com.example.demo.timeblock.controller;

import com.example.demo.timeblock.dto.StatusUpdateRequest;
import com.example.demo.timeblock.dto.TimeBlockRequest;
import com.example.demo.timeblock.dto.TimeBlockResponse;
import com.example.demo.timeblock.dto.TimelineResponse;
import com.example.demo.timeblock.model.TimeBlockStatus;
import com.example.demo.timeblock.pattern.dto.PatternInstanceResponse;
import com.example.demo.timeblock.pattern.dto.PatternRequest;
import com.example.demo.timeblock.pattern.dto.PatternResponse;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.HttpClientErrorException;
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

	@Test
	@DisplayName("returns 409 and standard error envelope on time block conflict")
	void conflictReturnsStandardError() throws Exception {
		LocalDate date = LocalDate.now();

		HttpHeaders headers = new HttpHeaders();
		headers.set(USER_HEADER, "error-user");

		TimeBlockRequest first = new TimeBlockRequest(
				LocalTime.of(9, 0),
				LocalTime.of(10, 0),
				"First",
				"#111",
				"one",
				null,
				null
		);
		ResponseEntity<TimeBlockResponse> created = restTemplate.exchange(
				uri("/api/timeline/" + date + "/blocks"),
				HttpMethod.POST,
				new HttpEntity<>(first, headers),
				new ParameterizedTypeReference<>() {}
		);
		assertThat(created.getStatusCode()).isEqualTo(HttpStatus.CREATED);

		TimeBlockRequest overlap = new TimeBlockRequest(
				LocalTime.of(9, 30),
				LocalTime.of(10, 30),
				"Overlap",
				"#222",
				"two",
				null,
				null
		);

		try {
			restTemplate.exchange(
					uri("/api/timeline/" + date + "/blocks"),
					HttpMethod.POST,
					new HttpEntity<>(overlap, headers),
					new ParameterizedTypeReference<TimeBlockResponse>() {}
			);
		} catch (HttpClientErrorException e) {
			assertThat(e.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);

			DocumentContext body = JsonPath.parse(e.getResponseBodyAsString());
			assertThat((int) body.read("$.status")).isEqualTo(409);
			assertThat((String) body.read("$.error")).isEqualTo("Conflict");
			assertThat((String) body.read("$.code")).isEqualTo("TIMEBLOCK_CONFLICT");
			assertThat((String) body.read("$.message")).isNotBlank();
			assertThat((String) body.read("$.path")).startsWith("/api/timeline/" + date + "/blocks");
			assertThat((String) body.read("$.timestamp")).isNotBlank();
			return;
		}

		throw new AssertionError("Expected 409 Conflict");
	}

	@Test
	@DisplayName("returns 400 and standard error envelope on request validation failure")
	void validationReturnsStandardError() {
		LocalDate date = LocalDate.now();

		HttpHeaders headers = new HttpHeaders();
		headers.set(USER_HEADER, "validation-user");

		TimeBlockRequest invalid = new TimeBlockRequest(
				LocalTime.of(9, 0),
				LocalTime.of(10, 0),
				"",
				"#111",
				"one",
				null,
				null
		);

		try {
			restTemplate.exchange(
					uri("/api/timeline/" + date + "/blocks"),
					HttpMethod.POST,
					new HttpEntity<>(invalid, headers),
					new ParameterizedTypeReference<TimeBlockResponse>() {}
			);
		} catch (HttpClientErrorException e) {
			assertThat(e.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

			DocumentContext body = JsonPath.parse(e.getResponseBodyAsString());
			assertThat((int) body.read("$.status")).isEqualTo(400);
			assertThat((String) body.read("$.error")).isEqualTo("Bad Request");
			assertThat((String) body.read("$.code")).isEqualTo("VALIDATION_ERROR");
			assertThat((String) body.read("$.message")).isNotBlank();
			assertThat((String) body.read("$.path")).startsWith("/api/timeline/" + date + "/blocks");
			assertThat((String) body.read("$.timestamp")).isNotBlank();

			assertThat(body.read("$.details.length()", Integer.class)).isGreaterThan(0);
			return;
		}

		throw new AssertionError("Expected 400 Bad Request");
	}

	@Test
	@DisplayName("returns 400 and standard error envelope on malformed JSON")
	void malformedJsonReturnsStandardError() {
		LocalDate date = LocalDate.now();

		HttpHeaders headers = new HttpHeaders();
		headers.set(USER_HEADER, "malformed-user");
		headers.setContentType(MediaType.APPLICATION_JSON);

		String invalidJson = "{";

		try {
			restTemplate.exchange(
					uri("/api/timeline/" + date + "/blocks"),
					HttpMethod.POST,
					new HttpEntity<>(invalidJson, headers),
					new ParameterizedTypeReference<TimeBlockResponse>() {}
			);
		} catch (HttpClientErrorException e) {
			assertThat(e.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

			DocumentContext body = JsonPath.parse(e.getResponseBodyAsString());
			assertThat((int) body.read("$.status")).isEqualTo(400);
			assertThat((String) body.read("$.error")).isEqualTo("Bad Request");
			assertThat((String) body.read("$.code")).isEqualTo("MALFORMED_JSON");
			assertThat((String) body.read("$.message")).isNotBlank();
			assertThat((String) body.read("$.path")).startsWith("/api/timeline/" + date + "/blocks");
			assertThat((String) body.read("$.timestamp")).isNotBlank();
			return;
		}

		throw new AssertionError("Expected 400 Bad Request");
	}

	@Test
	@DisplayName("returns 500 and standard error envelope on unexpected errors")
	void internalErrorReturnsStandardError() {
		HttpHeaders headers = new HttpHeaders();
		headers.set(USER_HEADER, "boom-user");

		try {
			restTemplate.exchange(
					uri("/api/test/boom"),
					HttpMethod.GET,
					new HttpEntity<>(headers),
					new ParameterizedTypeReference<String>() {}
			);
		} catch (org.springframework.web.client.HttpServerErrorException e) {
			assertThat(e.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);

			DocumentContext body = JsonPath.parse(e.getResponseBodyAsString());
			assertThat((int) body.read("$.status")).isEqualTo(500);
			assertThat((String) body.read("$.error")).isEqualTo("Internal Server Error");
			assertThat((String) body.read("$.code")).isEqualTo("INTERNAL_ERROR");
			assertThat((String) body.read("$.message")).isNotBlank();
			assertThat((String) body.read("$.path")).isEqualTo("/api/test/boom");
			assertThat((String) body.read("$.timestamp")).isNotBlank();
			return;
		}

		throw new AssertionError("Expected 500 Internal Server Error");
	}

	@Test
	@DisplayName("returns 404 and standard error envelope when resource is not found")
	void notFoundReturnsStandardError() {
		LocalDate date = LocalDate.now();

		HttpHeaders headers = new HttpHeaders();
		headers.set(USER_HEADER, "missing-user");

		TimeBlockRequest request = new TimeBlockRequest(
				LocalTime.of(9, 0),
				LocalTime.of(10, 0),
				"Update",
				"#111",
				"one",
				null,
				null
		);

		try {
			restTemplate.exchange(
					uri("/api/blocks/999999"),
					HttpMethod.PUT,
					new HttpEntity<>(request, headers),
					new ParameterizedTypeReference<TimeBlockResponse>() {}
			);
		} catch (HttpClientErrorException e) {
			assertThat(e.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

			DocumentContext body = JsonPath.parse(e.getResponseBodyAsString());
			assertThat((int) body.read("$.status")).isEqualTo(404);
			assertThat((String) body.read("$.error")).isEqualTo("Not Found");
			assertThat((String) body.read("$.code")).isEqualTo("RESOURCE_NOT_FOUND");
			assertThat((String) body.read("$.message")).isNotBlank();
			assertThat((String) body.read("$.path")).isEqualTo("/api/blocks/999999");
			assertThat((String) body.read("$.timestamp")).isNotBlank();
			return;
		}

		throw new AssertionError("Expected 404 Not Found");
	}

	private String uri(String path) {
		return "http://localhost:" + port + path;
	}
}
