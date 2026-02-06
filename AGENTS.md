# AGENTS.md

> Version: 1.1  
> Status: Stable (Quality Reinforced)

This document defines how the AI agent MUST behave during all development work.
The agent is not autonomous. All development is approval-driven, phase-based,
and strictly controlled by this protocol.

The agent MUST follow these rules at all times.

---

## 1. Global Rules (Always Applied)

- MUST: Ask the user for confirmation before starting any development work.
- MUST: Always follow the instructions defined in [`plan.md`](/plan.md).
- MUST: Follow [`skills/codex-operations/SKILL.md`](./skills/codex-operations/SKILL.md) for exploration, edits, command execution, and Git/GitHub operations.
- MUST NOT: Write production code without explicit user approval.
- MUST NOT: Skip phases or act autonomously.
- MUST: Stop immediately and ask the user if requirements are unclear or incomplete.
- MUST: Prioritize correctness, clarity, and user intent over speed.

### Git & GitHub Usage
- MUST: Use GitHub MCP for **Issue and Pull Request creation** and GitHub UI actions
  (e.g., PR comments, reviews).
- SHOULD: Use local git for **branch / add / commit / push** to keep local ↔ remote
  references in sync.
- MAY: Use GitHub MCP for push/branch operations **only if**
  - local git push is blocked (permissions, sandbox, network), or
  - the user explicitly requests it.

---

## 2. Development States (Finite State Machine)

The agent operates strictly in the following states:

1. **Plan Mode**
2. **TDD Mode**
3. **Development Mode**
4. **Verification Mode**
5. **Git Push Mode**

The agent MUST NOT reorder, skip, or merge states.

---

## 3. Plan Mode (Mandatory Entry State)

### Purpose
Analyze the problem and design the solution before any code is written.

### Rules
- MUST: Always start all work in Plan Mode.
- MUST: Break the problem into clear development units.
- MUST: Perform planning analysis and development design only.
- MUST NOT: Write tests or implementation code.

### Deliverables
The agent MUST create or update a [`plan.md`](/plan.md) file
that captures the current understanding of the problem and solution.

Typical contents MAY include (but are not limited to):
- Problem analysis
- Requirements or constraints
- Design or architectural considerations
- Tasks, checks, or execution steps
- Risks, assumptions, or open questions

### Exit Conditions
- MUST: Present [`plan.md`](/plan.md) to the user.
- MUST: Wait for explicit user approval before proceeding.
- MUST: After approval, register the problem described in [`plan.md`](/plan.md)
  as a GitHub Issue using GitHub MCP.
  - The Issue format MUST follow
    [`./skills/git-conventions/SKILL.md`](./skills/git-conventions/SKILL.md).
- MUST: Create a working branch according to git conventions **before** entering TDD Mode.
- MUST: Perform all tests and development on the working branch.
- MUST NOT: Work directly on `main`.

---

## 4. TDD Runtime Execution Protocol (TDD Mode)

### Purpose
Develop functionality using strict Test-Driven Development,
following Kent Beck’s TDD and Tidy First principles.

### Trigger
- The agent MAY proceed **only** when the user explicitly says **"go"**.

### TDD Methodology Guidance
- Start by writing a failing test that defines a small increment of functionality.
- Use meaningful test names that describe behavior
  (e.g., `shouldSumTwoPositiveNumbers`).
- Make test failures clear and informative.
- Write just enough code to make the test pass — no more.
- Once tests pass, consider whether refactoring is needed.
- Repeat the cycle for new functionality.
- When fixing a defect:
  - First write an API-level failing test.
  - Then write the smallest possible test that reproduces the defect.
  - Make both tests pass.

### Test Selection Rules
- MUST: Identify the **next uncompleted item** (e.g., test, task, or check)
  defined in [`plan.md`](/plan.md).
- MUST: Implement that test first.
- MUST: Mark the test as completed **only after it passes**.

### Tidy First Enforcement
- MUST: Separate changes into:
  - **Structural changes** (renaming, moving, refactoring without behavior change)
  - **Behavioral changes** (new or modified functionality)
- MUST NOT: Mix structural and behavioral changes in the same commit.
- MUST: Perform structural changes **only when all tests are passing**.

### Exit Conditions
- MUST: Remain in TDD Mode until the user explicitly approves moving forward.
- MUST: Ask for approval before leaving TDD Mode.
- MUST NOT: Write implementation code before completing TDD Mode.

---

## 5. Development Mode

### Purpose
Implement production code based on approved plans and tests.

### Rules
- MUST: Implement code incrementally in small steps.
- MUST: Follow all coding standards and architecture guidelines.
- MUST NOT: Deviate from the approved [`plan.md`](/plan.md) or TDD definitions.

### Development Loop
The agent MUST iterate through the following loop:
```Implement → Self-Verify → Run Tests → Fix Issues → Repeat```

---

## 6. Verification Mode

### Purpose
Ensure correctness, completeness, and quality before submission.

### Rules
- MUST: Verify all requirements defined in [`plan.md`](/plan.md).
- MUST: Run all tests.
- MUST: Confirm that no unintended behavior changes were introduced.

### Exit Conditions
- MUST: If any verification step fails, return to **Development Mode**.
- MUST: Proceed only when all verifications pass.

---

## 7. Git Push Mode

### Purpose
Finalize and submit the work to the remote repository.

### Rules
- MUST: Follow the conventions defined in
  [`./skills/git-conventions/SKILL.md`](./skills/git-conventions/SKILL.md).
- MUST: Ask for user confirmation before staging files.
- MUST NOT: Push directly to `main`; push from the working branch only.
- MUST: If conflicts occur, stop and ask for the user’s confirmation.

---

## 8. Read-Only Mode (Safety Guard)

### Purpose
Prevent accidental modifications during review or explanation.

### Rules
The agent MUST enter Read-Only Mode when:
- Summarizing or reviewing existing code.
- Answering questions about the current state.
- Explicitly requested by the user (e.g., "review", "explain").

While in Read-Only Mode:
- MUST NOT: Modify any files.
- MUST NOT: Run commands that change repository state.
- MUST: Only read files and report findings.
- MUST: Explicitly state that it is operating in Read-Only Mode.

---

## 9. Scope Guard (Plan Boundary Enforcement)

- MUST: Treat [`plan.md`](/plan.md) as the authoritative boundary
  of the currently approved scope of work.
- MUST NOT: Implement features, tests, or refactors not explicitly defined
  in [`plan.md`](/plan.md).
- MUST: If a new requirement or improvement is identified, stop and ask the user
whether to update [`plan.md`](/plan.md) before proceeding.

---

## 10. Skill Precedence (When Multiple Skills Apply)

When multiple skills could apply, follow this precedence to avoid conflicts:

1) **Operations First**: `skills/codex-operations/SKILL.md` governs tool usage and safety.
2) **Workflow Next**: `skills/git-conventions/SKILL.md` governs Git/GitHub workflow and PR requirements.
3) **TDD Script First**: `skills/tdd-workflow/SKILL.md` defines how to write a feature-specific TDD script before implementation.
4) **Process Before Stack**: `skills/tdd-loop/SKILL.md` defines the generic TDD loop (RED→GREEN→REFACTOR).
5) **Stack-Specific Overlay**: use `skills/springboot-tdd/SKILL.md` for Spring Boot test patterns and conventions.
5) **Coding Standards**: use `skills/java-coding-standards/SKILL.md` for Java style and maintainability.
6) **Persistence Guidance**: use `skills/jpa-patterns/SKILL.md` for JPA/Hibernate patterns.
7) **Architecture Reference**: use `skills/backend-patterns/SKILL.md` only when architectural decisions are needed.

If any two skills disagree, the earlier (higher priority) item wins, unless the user explicitly overrides.

---

## 10. Change Budget Rule

To maintain small, reviewable changes:

- MUST: Limit each TDD iteration to the smallest possible change.
- SHOULD: Avoid modifying more than one logical unit
  (function, class, or module) per iteration.
- MUST: If a change grows beyond a small, clear scope,
  stop and split the work into additional steps
  or update [`plan.md`](/plan.md).

---

## 11. Final Principle

The agent MUST behave as a disciplined engineering partner,
not an autonomous code generator.

If any rule conflicts or uncertainty arises,
the agent MUST stop and ask the user.
