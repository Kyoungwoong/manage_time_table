---
name: assessment-context-skill
description: Capture MCP problem statements, constraints, and follow-up questions before coding in a time-limited assessment.
---

# Context 정리 Skill

## Goal
모든 작업에 앞서 MCP에서 받은 문제 설명, 예제, 제약 조건을 한눈에 파악할 수 있도록 정리한다. 이후 계획/코딩/테스트 단계에서 논리적 흐름을 놓치지 않도록 돕는다.

## Workflow
1. README, TODO, MCP 문서를 빠르게 읽고 문제 목적, 입력/출력, 제약 조건을 한 문단으로 요약해 README 또는 작업 코멘트에 기록한다.
2. 이해가 불확실한 부분은 `질문/확인 필요` 항목으로 모아서 평가자 또는 MCP에 다시 문의할 질문을 정리한다.
3. 필요한 추가 예제를 `mcp` 명령으로 받아 `EXAMPLE.md` 등에 저장하고, 핵심 케이스를 간단히 요약해 둔다.
4. 시간/메모리/데이터 크기 제약과 핵심 로직 흐름을 도식화(간단한 리스트/표/순서도)하여 머릿속으로 전체 구조를 가늠한다.
5. 정리된 내용을 토대로 README TODO를 업데이트하고 `skills/problem-solving.md`, `skills/testing.md`에 따라 이후 단계를 진행한다.

## Handoff
- 새로운 정보를 얻을 때마다 이 문서를 다시 참조하여 요약을 갱신하고 질문 목록을 좁힌다.
- 시작 전에 나온 가정(입력 범위, 정렬된 여부 등)을 명시적으로 적어두고, 코드/테스트 단계에서 다시 검증한다.

이 Skill은 타임박스된 테스트에서 요구사항을 놓치지 않도록 도와준다.
