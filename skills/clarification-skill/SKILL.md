---
name: assessment-clarification-skill
description: Capture ambiguous requirements, list questions, and surface them early during time-constrained assessments.
---

# Clarification Skill

## Goal
Make ambiguous or missing requirements visible before coding so you can either choose a safe default or ask the evaluator.

## Procedure
1. After reading the prompt, note every statement that lacks detail (ranges, formats, edge behaviors) in a “questions/assumptions” list.
2. For each item, write a short hypothesis (“Assume list items are sorted”) and the minimal impact if the hypothesis is wrong.
3. If the assessment rules allow, ask the evaluator for clarifications by quoting the exact language and offering two possible interpretations.
4. When clarifications are unavailable, encode the chosen assumption near the related code or README TODO, so the reviewer sees why you proceeded.
5. Revisit the list after every major change; mark items as resolved, blocked, or accepted defaults.

## Handoff
- Include the open questions and chosen defaults in README TODOs or PR description so the reviewer can double-check them.
- If a clarification triggers a change, update the log and note how it affects previously written code/tests.
