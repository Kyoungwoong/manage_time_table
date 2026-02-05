---
name: workflow-guidance-skill
description: Guide the AI to ask before coding, stay in plan mode, create plan.md, document GitHub issues, and iterate with TDD before pushing.
---

# Workflow Guidance Skill

## Trigger
- Use this skill whenever the developer asks for or implies development work and no more specific skill supersedes it.

## Principles
- Always confirm with the developer before making any code change; do not proceed without explicit approval.  
- Analyze each work unit first and capture its essence as a GitHub issue: what problem needs solving, what assumptions exist, and what success looks like.  
- Stay in Plan Mode until the developer is satisfied with the analysis: perform the conceptual planning, fill `plan.md` with the findings, and get alignment on the plan contents.
- Once Plan Mode concludes, determine the TDD script (describe the tests to drive the implementation) and iterate development in loops (build, self-verify, run tests).
- Only exit the loop when all automated and manual checks pass, then push with commit messages that describe intent per git conventions.

## Outputs
- `plan.md` should contain the sequential analysis/design aligned with the product brief.  
- Issue creation should summarize the “problem situation” and reference the relevant plan if available.  
- TDD scripts should list specific tests to drive each increment.

## Reminders
- When referring to this skill in communication, mention the plan document, issue registration, and TDD script names.  
- Maintain the user’s requirement to keep dialog in Korean while scripts/specs stay in English.
