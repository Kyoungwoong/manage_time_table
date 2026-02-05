---
name: assessment-review-skill
description: Perform a quick self-review before finalizing work: check correctness, document assumptions, and keep the code understandable.
---

# Self-Review Skill

## Checklist
1. Does the code solve the stated problem? Re-read the requirement and confirm all cases are covered.
2. Are assumptions (input ranges, guaranteed invariants) documented near the relevant code or in README TODO?
3. Can another engineer understand the solution in five minutes? Keep logic and naming explicit.
4. Is there anything surprising, implicit, or fragile? Add comments or simplify to eliminate it.

## Practice
- Run through at least one main scenario and one edge case mentally, highlighting exactly where each decision is made.
- If you identify a potential regression, note it explicitly and suggest a follow-up test or fix.
