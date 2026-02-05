---
name: mcp-setup-skill
description: Run once per assessment to connect to MCP, fetch problem docs, and record any blockers before starting coding.
---

# MCP 연결 Skill

## Goal
문제 문서를 MCP에서 안전하게 받아 작업에 필요한 정보를 확보하고, 처음 연결 시 누락된 권한/토큰 이슈를 곧바로 파악한다.

## Checklist
1. README나 과제 안내에서 추천 MCP 이름(예: `practice_musinsa` 워크스페이스)과 URL을 확인하여 어떤 서버/문서를 사용할지 정리한다.
2. 인증 토큰/키가 필요하면 개인 설정 저장소에만 기록하고, 깃 커밋에는 절대 포함하지 않는다.
3. `mcp fetch PROBLEM.md`, `mcp list docs` 등의 명령으로 필요한 문서를 받아 README TODO를 최신화하고, `skills/context.md`를 따라 문제 목적·입출력·제약을 정리한다.
4. 추가로 필요한 예제가 있으면 `mcp fetch EXAMPLE.md`(또는 적절한 이름)로 받아 저장한다.
5. 연결 상태, 접근 권한, 문서 위치 등을 간단히 메모하여 나중에 다시 접속할 때 참고한다.

## Troubleshooting
- 서버 연결 문제나 권한 부족은 평가자/운영자에게 문의하고 그 기록을 README TODO나 질문 목록에 남긴다.
- 변경된 API/문서 위치가 있으면 바로 `skills/context.md`에서 인용하여 전체 맥락을 놓치지 않도록 한다.
