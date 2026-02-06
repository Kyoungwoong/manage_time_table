# Skills Index

This folder contains process and guidance documents used by humans and AI agents.

## Precedence (high → low)

1. `codex-operations/SKILL.md` — Tool usage + safety (always follow)
2. `git-conventions/SKILL.md` — Git/GitHub workflow + PR conventions
3. `tdd-workflow/SKILL.md` — Write a feature-specific TDD script (docs-first)
4. `tdd-loop/SKILL.md` — Generic TDD loop (RED→GREEN→REFACTOR)
5. `springboot-tdd/SKILL.md` — Spring Boot specific testing patterns
6. `java-coding-standards/SKILL.md` — Java style and maintainability
7. `jpa-patterns/SKILL.md` — JPA/Hibernate patterns
8. `backend-patterns/SKILL.md` — Backend architecture patterns (use when needed)

If two skills conflict, follow the earlier item unless the user explicitly overrides.

## Notes

- Keep examples accurate for this repository’s primary stack (Java/Spring/Gradle).
- Prefer short, enforceable rules over long essays.
