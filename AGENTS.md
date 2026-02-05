---
name: development-workflow
Description: Development workflow for AI agents. A systematic development process including Plan mode analysis, TDD-based development, and self-verification loops.
---

# Development Workflow

## core principles
- **Be sure to get confirmation from the user before all development work**
- **Proceed step by step and obtain approval after completion of each step**

---

## Phase 1: Analysis and Planning (Plan Mode)

### 1.1 Requirements Analysis
1. Break down user requests in units
2. Organize what you want each unit to develop
3. Derive problem situations and solutions

### 1.2 Register GitHub Issue
- Register analyzed problem situations as Issue
- For more information on Git, see the '@git-workflow' skill

### 1.3 Create Plan.md
```markdown
# Plan: [Function name]

## 1. Planning Analysis
- Purpose:
- User Scenario:
- Constraints:

## 2. Development Design
- Architecture:
- Required Modules/Files:
- Data Flow:

## 3. Work unit decomposition
- [ ] Task 1: ...
- [ ] Task 2: ...

## 4. Expected Risk
- ...
```
⚠️ Proceed to the next step after user approval

## Phase 2: TDD (Test-Driven Development)
### 2.1 TDD Cycle
```
RED → GREEN → REFACTOR → REPEAT
```
### 2.2 Checklist
- [ ] Test presence for all requirements
- [ ] Includes edge case test
- [ ] Include error handling tests
  ⚠️ Continue developing until the user says "OK"
---

## Phase 3: 개발 (Development Loop)
### 3.1 Development Loop
```
Write code → Self-verify → Test → Repeat on failure
```

### 3.2 Self-Verification Items
- [ ] Code Style / Convention Compliance
- [ ] Error handling appropriate
- [ ] No performance issues
  ⚠️ Proceed to the next step after passing all tests

## Phase 4: Git Push
Push according to @git-workflow skill when all tests pass

## Workflow Summary
```
[1] Analysis → Issue → Plan.md → Approve
[2] TDD → Test Creation → Repeat until approval
[3] Development → Self-Verify → Test → Repeat to Pass
[4] Git Push (@git-workflow) → 완료
```
## an important rule
* never proceed arbitrarily
* Make sure to get user approval after completion of each phase
* If anything is missing, I'll report it immediately
* do not write code without testing

---