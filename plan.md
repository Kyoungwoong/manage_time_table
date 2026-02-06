# Plan (Current Task Only): PR Conventions + Branch-First Workflow

This `plan.md` intentionally contains **only the currently active task**.  
It is used to create a GitHub Issue first; `plan.md` is **not intended to be pushed**.

---

## 1) Problem Analysis

We want to reduce recurring local/remote divergence (“pull conflicts”) and make collaboration consistent by:
1) Standardizing a high-signal PR title/body template.
2) Enforcing a branch-first workflow (create a working branch before tests/development/push).

Currently:
- `skills/git-conventions/SKILL.md` has basic Issue/Commit/Branch rules, but PR conventions are minimal.
- `AGENTS.md` does not explicitly require branch creation **before** TDD/Development, and does not state whether GitHub operations must use GitHub MCP.

---

## 2) Functional Requirements

### 2.1 PR conventions (spec)
Update `skills/git-conventions/SKILL.md` to include:
- PR title format: `[Feature|Bug|Refactor|Docs|Chore] Brief description`
- PR body template including:
  - Background
  - What changed
  - How to test
  - Risk / Rollback
  - Non-goals (scope boundaries)
  - Checklist (tests, unrelated files, docs)
  - Issue connection line: `Closes #<issue-number>`

### 2.2 PR template file (GitHub UI)
Add `.github/pull_request_template.md` using the same template so it auto-populates in GitHub UI.

### 2.3 Branch-first workflow (conflict reduction)
Update `AGENTS.md` to explicitly require:
- After issue creation (Plan Mode exit), create a working branch **before** entering TDD Mode.
- All development/testing happens on that branch (not on `main`).
- GitHub operations (branch creation, commits, pushes, PRs) must be done via **GitHub MCP**.
- After merge, local sync guidance: hard-sync `main` to `origin/main` when needed (user-driven).

---

## 3) Non-Functional Constraints

- Keep rules concise and unambiguous.
- Avoid over-prescriptive process that blocks doc-only changes.
- Do not contradict the existing phase FSM (Plan → TDD → Dev → Verify → Git Push).

---

## 4) Design Decisions

- Put the PR template source-of-truth in `.github/pull_request_template.md`.
- Keep `skills/git-conventions/SKILL.md` as the narrative spec and reference the template file.
- Add a “branch-first” gate into Plan Mode Exit Condition (create branch before TDD).

---

## 5) Task Breakdown

1. Update `skills/git-conventions/SKILL.md` with PR conventions and reference to `.github/pull_request_template.md`.
2. Add `.github/pull_request_template.md`.
3. Update `AGENTS.md` with branch-first + “use GitHub MCP for GitHub operations”.
4. Register this plan as a GitHub Issue (GitHub MCP).
5. Push changes on a branch + open PR (GitHub MCP).

---

## 6) Risks & Assumptions

- Risk: Branch naming rules may conflict with an agent-required `codex/` prefix; clarify precedence in `skills/git-conventions/SKILL.md`.
- Assumption: The team wants PRs to be self-contained, with explicit test instructions and risk notes.
