# AGENTS.md

This document defines how the AI agent MUST behave during all development work.
The agent is not autonomous. All development is approval-driven, phase-based,
and strictly controlled by this protocol.

The agent MUST follow these rules at all times.

---

## 1. Global Rules (Always Applied)

- MUST: Ask the user for confirmation before starting any development work.
- MUST: Always follow the instructions defined in [`plan.md`](./plan.md).
- MUST NOT: Write production code without explicit user approval.
- MUST NOT: Skip phases or act autonomously.
- MUST: Stop immediately and ask the user if requirements are unclear or incomplete.
- MUST: Prioritize correctness, clarity, and user intent over speed.
- MUST: Use GitHub MCP for GitHub operations (Issue/Branch/Commit/Push/PR).

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
The agent MUST create a [`plan.md`](./plan.md) file containing:
- Problem analysis
- Functional requirements
- Non-functional constraints
- Architecture / design decisions
- Task breakdown
- Identified risks and assumptions

### Exit Condition
- MUST: Present [`plan.md`](./plan.md) to the user.
- MUST: Wait for explicit user approval before proceeding.
- MUST: If the user approved this [`plan.md`](./plan.md), register the problem described in [`plan.md`](./plan.md) as a GitHub Issue using GitHub MCP.
  - The Issue format is file at [`./skills/git-conventions/SKILL.md`](./skills/git-conventions/SKILL.md) 
- The Issue MUST reflect the contents of [`plan.md`](./plan.md).
- MUST: After Issue creation, create a working branch (per `skills/git-conventions`) **before** entering TDD Mode.
- MUST: All tests/development happen on that branch (do not work directly on `main`).
- MUST-NOT: DO NOT Write After TDD phase (including Development)

---

## 4. TDD Runtime Execution Protocol (TDD Mode)

### Purpose
Develop functionality using strict Test-Driven Development
following Kent Beck’s TDD and Tidy First principles.

### Trigger
- When the user explicitly says **"go"**, the agent MAY proceed within this mode.

### TDD METHODOLOGY GUIDANCE
- Start by writing a failing test that defines a small increment of functionality
- Use meaningful test names that describe behavior (e.g., "shouldSumTwoPositiveNumbers")
- Make test failures clear and informative
- Write just enough code to make the test pass - no more
- Once tests pass, consider if refactoring is needed
- Repeat the cycle for new functionality
- When fixing a defect, first write an API-level failing test then write the smallest possible test that replicates the problem then get both tests to pass.

### Test Selection Rule
- MUST: Identify the **next unmarked test** in [`plan.md`](./plan.md).
- MUST: Implement that test first.
- MUST: Mark the test as completed only after it passes.

### Tidy First Enforcement
- MUST: Separate changes into:
  - **Structural changes** (renaming, moving, refactoring)
  - **Behavioral changes** (new or changed functionality)
- MUST NOT: Mix structural and behavioral changes in the same commit.
- MUST: Perform structural changes only when tests are passing.

### Exit Condition
- MUST: Remain in TDD Mode until the user explicitly approves moving forward.
- MUST: Ask for approval before leaving TDD Mode.

---

## 5. Development Mode

### Purpose
Implement production code based on approved plans and tests.

### Rules
- MUST: Implement code incrementally in small steps.
- MUST: Follow all coding standards and architecture guidelines.
- MUST NOT: Deviate from the approved [`plan.md`](./plan.md) or TDD definitions.

### Development Loop

---
## 6. GitHub push

### Purpose
Add worked file and commit into remote repository.

### Rules
- MUST: Follow the convention in [`./skills/git-conventions/SKILL.md`](./skills/git-conventions/SKILL.md)
- MUST: Check user's confirm before file add.
