# manage_time_table (TimeWeave)

TimeWeave is a timeline-first time table manager that treats every day as a flow of blocks instead of isolated events.

## What’s in this repo
- **Spring Boot + Spring Data JPA backend** serving REST APIs for creating, editing, and tracking time blocks.
- **H2 in-memory database** (configured for local runs) with basic validation and status tracking for each block (PLANNED → PARTIAL/COMPLETED/MISSED).
- **TimeBlock CRUD service** that enforces non-overlapping windows, stores metadata (title, color, icon, pattern link, notes), and computes recap metrics per day.

## Notable endpoints
- `POST /api/timeline/{date}/blocks`: create a block with status `PLANNED`.
- `GET /api/timeline/{date}`: fetch ordered blocks plus recap metrics (planned/actual minutes, partial/missed counts).
- `PUT /api/blocks/{id}` / `PATCH /api/blocks/{id}/status`: edit metadata or transition status.
- `DELETE /api/blocks/{id}`: remove a scheduling block.

## Tech & tooling
- Spring Boot 4.0.2 with Gradle, Lombok, and Jakarta Validation.
- H2 (in-memory) for rapid iteration and tests (auto-created schema via `spring.jpa.hibernate.ddl-auto=update`).
- Tests powered by `spring-boot-starter-test` and Apache HTTP client for integration coverage.

## Running
1. `./gradlew test` — runs unit and integration suites covering TimeBlock creation, validation, recap metrics, and controller flows.
2. After tests pass, use `git add README.md && git commit -m "docs: expand README for TimeBlock CRUD" && git push origin main`.

## Workflow
- Issue tracking is managed via GitHub (e.g., [Document README for TimeBlock CRUD](https://github.com/Kyoungwoong/manage_time_table/issues/1)).
- Follow the Plan Mode → TDD → validation loop before adding new features.
