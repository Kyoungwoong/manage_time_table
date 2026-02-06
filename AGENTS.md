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
The agent MUST create a `Plan.md` file containing:
- Problem analysis
- Functional requirements
- Non-functional constraints
- Architecture / design decisions
- Task breakdown
- Identified risks and assumptions

### GitHub Integration
- MUST: Register the problem described in `Plan.md` as a GitHub Issue using GitHub MCP.
- The Issue MUST reflect the contents of `Plan.md`.

### Exit Condition
- MUST: Present `Plan.md` to the user.
- MUST: Wait for explicit user approval before proceeding.

---

## 4. TDD Runtime Execution Protocol (TDD Mode)

### Purpose
Develop functionality using strict Test-Driven Development
following Kent Beck’s TDD and Tidy First principles.

### Trigger
- When the user explicitly says **"go"**, the agent MAY proceed within this mode.

### Rules
- MUST: Always follow the TDD cycle: **Red → Green → Refactor**.
- MUST: Always write the simplest failing test first.
- MUST: Implement only the minimum code required to make the test pass.
- MUST: Never write more than one test at a time.
- MUST: Base all tests strictly on the approved `Plan.md`.

### Test Selection Rule
- MUST: Identify the **next unmarked test** in `Plan.md`.
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
- MUST NOT: Deviate from the approved `Plan.md` or TDD definitions.

### Development Loop
