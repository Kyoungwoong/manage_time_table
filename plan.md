# TimeWeave (TimeBlock Planner) — Plan

## 1. Requirements Recap (sequentially aligned with PROBLEM.md)

1. **Service Overview**
   - Deliver a lightweight “TimeWeave” experience that treats a day as a flow of time blocks rather than calendar events.
   - Address repeated frustration with event-first tools by offering a timeline-first planner with a softer tone and playful UX.
2. **Pain Points**
   - Users cannot visually grasp a whole day from event-based calendars.
   - Time-blocked routines (study, fitness, rest) are hard to manage across disparate tools.
   - Retrospectives feel emotional and anecdotal rather than data-driven.
3. **Target Users**
   - MVP: university students and self-managing routines.
   - Expansion: freelancers, creators, developers with recurring habits.
4. **Core Concept**
   - Time flows from 00:00 to 24:00 vertically; blocks represent intentions and actual execution states.
   - Plans remain editable; the tool encourages experimentation rather than perfection.
5. **Sequential Core Features** (mirrors PROBLEM sections 5 and 11)
   - (a) Daily timeline UI with drag-and-drop block creation.
   - (b) Repeatable patterns with weekday grouping and overrides.
   - (c) Check-based actual usage tracking (completed/partial/missed).
   - (d) Daily recap summarizing plan vs. reality, plus memo input.
6. **UX Direction**
   - Minimal, pastel/muted palette, soft motion, low interaction cost, “makeable” feel.
7. **MVP Scope**
   - Includes: auth (email/social), timeline CRUD, repeat pattern persistence, recap summary.
   - Excludes: notifications, collaboration, timers (stated constraint).
8. **Extension Ideas**
   - Weekly/monthly pattern analysis, pattern recommendations, focus metrics, mobile client, integrations with external calendars/Notion.

## 2. Architecture & Interfaces

### Backend
- **Existing foundation**: Spring Boot skeleton (`DemoApplication`) will host REST services and persistence layers.
- **Key domains**:
  1. `TimeBlock` – stores planned start/end, title/icon, color label, and status (PLANNED, PARTIAL, COMPLETED, MISSED).
  2. `RepeatPattern` – weekday masks, tag (e.g., “weekday study”), start/end recurrence range, optional exception dates.
  3. `DailyReflection` – references a date, aggregates planned vs. actual totals, stores short memo.

### Services & DTOs
- `TimelineService`: CRUD operations for daily blocks + summary calculations.
- `PatternService`: create/read/update repeat templates, expand them into actual blocks for given dates, handle exceptions.
- `ReflectionService`: compute plan/actual ratios and store memo entries.
- DTOs: `TimeBlockRequest`, `TimeBlockResponse`, `PatternRequest`, `ReflectionResponse`, `RecapMetrics`.

### REST Endpoints (sketch)
1. `POST /api/timeline/{date}/blocks` – create a block (includes plan metadata and optional repeat reference).
2. `GET /api/timeline/{date}` – return ordered blocks with status badges + recap metrics.
3. `PATCH /api/blocks/{id}/status` – mark actual execution (completed/partial/missed).
4. `POST /api/patterns` – define a repeat set (weekdays, time range, color, icon).
5. `GET /api/patterns/{patternId}/instances?date=YYYY-MM-DD` – preview auto-applied blocks.
6. `POST /api/recaps/{date}` – submit memo and finalize reflection data.

### Persistence & State
- Entities should track transitions: planned blocks flagged with `status=PLANNED`; when user checks completion, update to `PARTIAL` or `COMPLETED`. `MISSED` for skipped blocks.
- Repeat expansion stores instances with pointer to pattern + `is_exception`.
- Graphical timeline should sort by start time, show color/icon per block, and display status overlay.
- Daily recap aggregates total planned minutes vs. completed minutes, plus number of partial/missed blocks.

### Constraints
- MVP avoids notifications, collaboration, and timers; storage remains simple (likely relational via Spring Data JPA).
- Authentication (email/social) is noted but implementation details deferred; default assumption is Spring Security with basic provider stub.
- UI is assumed to be a separate JS/SPA; backend should provide clear JSON payloads for timeline, repeat templates, and reflections.

## 3. Feature Interfaces & Data Flow

### Daily Timeline View
- **Front-end behavior**: vertical 00:00–24:00 timeline, drag/resize blocks, color-coded categories, status overlay (icons or pills). Blocks can open quick editors.
- **APIs**: GET timeline returns list of blocks + `recapMetrics` object. Each block includes start/end ISO times, label, color, icon, status enum, pattern reference.
- **Data**: store `startTime`, `endTime`, `category`, `status`, `patternId`, `notes`.

### Time Block CRUD
- **Interactions**: long-press drag to create, pinch or handles to adjust; detail form for title/icon/color and planned/actual indicators.
- **API**: POST/PUT block endpoints; PATCH status for actual check.
- **Data**: `status` transitions handled server-side to ensure valid sequences (PLANNED → PARTIAL/COMPLETED → final).

### Repeat Pattern Manager
- **Interactions**: form for weekday selection (e.g., Weekday/Weekend), duration, color/icon, auto-apply toggle, exception dates list.
- **API**: manage patterns (CRUD) and `GET /instances` to preview/reschedule, `POST /patterns/{id}/exceptions`.
- **Logic**: server expands patterns into actual `TimeBlock` instances per date, marking exceptions as `isException=true`.

### Daily Recap Summary
- **Interactions**: summary chips (planned vs. actual %, partial count), short memo input field (optional), save button.
- **API**: `GET /recaps/{date}` to load metrics; `POST /recaps/{date}` to submit memo.
- **Data**: `recapMetrics` includes `plannedMinutes`, `completedMinutes`, `partialCount`, `missedCount`, `memo`.

## 4. Implementation Sequence

1. **Timeline UI baseline** – design 00:00–24:00 scaffold, placeholder blocks (frontend work). Backend: timeline endpoint returning empty structure.
2. **Block CRUD service** – implement `TimeBlock` entity/service/repository, POST/PUT/PATCH endpoints, DTOs, validations.
3. **Persistence/loading** – ensure blocks persist and GET timeline populates board; handle auth stub to scope data per user.
4. **Repeat patterns** – Pattern entity/service, schedule manager, auto-expansion routine, exception handling, related API.
5. **Recap summary** – compute metrics (planned vs. actual), allow memo input, expose recap endpoint, connect to timeline response.

## 5. Engineering Notes

- **Stack**: Spring Boot backend (current Maven/Gradle setup), front-end to be determined (likely JS/SPA).
- **Persistence**: Relational database via Spring Data JPA; tables for `time_block`, `repeat_pattern`, `daily_reflection`.
- **Security**: MVP includes placeholder for email/social login; integrate Spring Security with JWT or session later.
- **Data Flow**: Controllers → Services → Repositories; service handles status logic (prevents overlapping planned blocks per user by validation).
- **Operational constraints**: Keep tooling minimal; avoid complex orchestration until MVP data flows stable.

## 6. Testing & Validation Guidance

### Automated Tests
1. **Timeline CRUD service** – unit test for create/read/update lifecycle + validation (no overlapping open blocks).
2. **Repeat engine** – unit tests verifying weekday masks, exception dates, and correct block instances.
3. **Recap calculator** – unit tests for summary percentages, partial/missed counters, memo persistence.

### API Contract Tests
- Block creation: POST timeline block and expect 201 with stored status default PLANNED.
- Status transitions: PATCH block status from PLANNED → COMPLETED/ PARTIAL/ MISSED; expect server-validated result.
- Pattern instance retrieval: GET pattern instances for date with expansion.

### Manual Acceptance
1. Create a time block by dragging; verify timeline displays block with color/icon.
2. Assign a repeat pattern (e.g., Weekday Study); confirm block auto-populates on target dates and respects exceptions.
3. Toggle “actual” status to partial/completed/missed; ensure recap metrics update accordingly.
4. Submit daily memo and verify recap summary reflects note plus calculated plan-vs-actual ratio.

### Edge Cases
- Overlapping blocks: service rejects simultaneous blocks that share time ranges unless intentional (allow manual override with warning).
- Exception dates: repeat pattern skips blocked dates and marks them as exceptions rather than regenerating duplicates.
- Status triangles: “Partial” vs. “Completed” vs. “Missed” should not regress to PLANNED without explicit user action; transitions must be explicit via API.

## 7. Assumptions & Defaults

- The plan document is descriptive; no implementation changes happen in this step.  
- Backend remains Spring Boot; frontend assumed to be built separately (JS/SPA).  
- Authentication strategy is noted (email/social) but implementation detail is deferred.  
- Follow PROBLEM.md order (problem → target → concept → features → UX → scope → roadmap) to keep plan sequentially aligned.  
- The MVP explicitly excludes notifications, collaboration, and timers, as noted in the brief.  
