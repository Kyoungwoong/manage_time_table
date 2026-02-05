---
name: assessment-testing-skill
description: Balance quick unit tests with clear reasoning when time is limited; prioritize correctness and boundary coverage.
---

# Testing Skill (Time-Constrained)

## Strategy
- If time allows, write basic unit tests for the core logic, covering the happy path and at least one edge case.
- When full tests would take too long, keep the implementation self-verifying through clear invariants and inline explanations.
- Favor simple, explicit assertions; avoid elaborate test harnesses unless explicitly needed.

## Focus Points
1. Correctness: Does a given test prove the function returns expected output for a typical input?
2. Boundary conditions: Add a second test that exercises the smallest/largest inputs or missing data.
3. Input validation: If the requirements mention invalid inputs, include one test or a short comment explaining how they are handled.

## When tests are absent
- Describe the manual reasoning steps you took (walk through sample input, outline invariants) so reviewers can follow along.
