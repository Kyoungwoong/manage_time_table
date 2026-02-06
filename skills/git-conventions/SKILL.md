---
name: git-workflow
description: Git Issue, Commit, Branch, Push 규칙을 정의하는 workflow skill
version: 1.0
---

# Git Workflow Skill

## 1. Purpose
This document defines the Git-based collaborative workflow used in the project.  
The purpose is to standardize Issue Management, Commit Message Rules, Branch Strategy, and Push/PR procedures.

---

## 2. Scope
- GitHub-based projects
- Applicable to both personal development and team collaboration
- Mandatory for all code change operations

---

## 3. Issue Management Rules

### 3.1 Issue Title Convention
- MUST: Follow the format below
```
[Feature|Bug|Refactor|Docs|Chore] Brief description
```
Example:
```
[Feature] Adding a user login feature
[Bug] Correct authentication token expiration error
```

---

### 3.2 Issue Description Template
Issue The body must include the following items.

- Background and Purpose
- Detailed requirements
- Estimated scope of work
- Related files or modules

---

## 4. Commit Convention

### 4.1 Commit Message Format
- MUST: Follow the following format

```
<type>(<scope>): <subject>
```

---

### 4.2 Commit Types

| Type     | Description                     |
|----------|---------------------------------|
| feat | Add new features |
| fix | fix bug | fix
| docs | document modification |
| style | Formatting, Semicolon, etc. Code style |
| factor | improvement of code structure without change of function |
| test | Add or modify a test |
| store | build, setup, other tasks |

---

### 4.3 Commit Message Example
```
feat(auth): Implementing the user login function
```

- Add JWT token-based authentication
- Create login API endpoints

---

## 5. Branch Strategy

### 5.1 Branch Naming Convention
- MUST: Follow the format below

```
<type>/<issue-number>-<short-description>
```
Example:
```bash
feat/123-user-login
fix/456-auth-token-bug
refactor/789-cleanup-utils
```

Notes:
- Agent-created branches may be prefixed with `codex/` (e.g., `codex/feat/123-user-login`) to match the agent’s branch prefix requirement.

---

## 6. Push & Pull Request Workflow

### 6.1 Pre-Push Checklist
- MUST: All tests passed
- MUST: Commit Convention 준수
- MUST: associated Issue number connection
- MUST: Don't commit unnecessary files

---

### 6.2 Git Commands

```bash
# Create Branch
git checkout -b <branch-name>

# Commit
git add .
git commit -m "<type>(<scope>): <subject>"

# Push
git push origin <branch-name>
```
### 6.3 Pull Request Rule

* MUST: Include Issue Connection Statement in PR Body
```
Closes #<issue-number>
```

### 6.4 Pull Request Title Convention
- MUST: Follow the same category prefix format as Issues
```
[Feature|Bug|Refactor|Docs|Chore] Brief description
```

### 6.5 Pull Request Body Template (Recommended)
PR body should include:
- Background and Purpose
- What changed (summary bullets)
- How to test (exact commands / steps)
- Risk / Rollback plan
- Non-goals (explicitly out of scope)
- Checklist
  - [ ] Tests passing (e.g., `./gradlew test`)
  - [ ] No unrelated files included
  - [ ] Docs updated (if needed)
- Issue connection: `Closes #<issue-number>`

### 6.6 Pull Request Template File
- Recommended: Add `.github/pull_request_template.md` so GitHub auto-populates PR descriptions.
