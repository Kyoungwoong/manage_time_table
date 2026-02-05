---
name: assessment-handoff-skill
description: Summarize work, highlight assumptions, and spell out remaining steps before leaving the assessment workspace.
---

# Handoff Skill

## Goal
Ensure that whoever reviews or inherits the work can pick up the context, assumptions, and next steps without re-digesting the full problem.

## Steps
1. Write a short summary of what you built, why it satisfies the requirement, and which skill/workflow guided the approach.
2. List key assumptions (input ordering, uniqueness, default values) and point to the code/location where they live.
3. Record remaining tasks (tests to run, edge cases to revisit) along with who owns them if known.
4. If you touched documentation or README TODOs, describe what changed and why.
5. Mention any blockers encountered and where the clarification log lives (`skills/clarification-skill` or README TODO).

## Final checks
- Before handing off, run one last manual scenario from `skills/problem-solving-skill` and note that it passed (or why it cannot).
- Confirm `skills/testing-skill` criteria have been addressed; note whether you used tests or manual reasoning.
- Points 2-5 should be captured in your README TODO, commit/PR description, or a dedicated `HANDOFF.md` if the repository uses one.
