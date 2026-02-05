---
name: assessment-git-skill
description: Follow small, focused commits with descriptive messages during time-limited assessments.
---

# Git Skill (Assessment Mode)

## Purpose
Keep commit history clear and reviewable by grouping related changes into one logical commit and explaining the reasoning behind each change.

## Commit guidance
- Commit only once a coherent chunk of work (e.g., “Implement order validation logic”) is ready; avoid capturing unrelated edits in the same snapshot.
- Use imperative titles that explain why the change exists, not just what changed.
- Double-check `git status` before committing to avoid adding temporary/test artifacts.

## Post-commit checks
- Squash only if commits exceed the minimal scope approved for this assessment; otherwise keep each commit separated.
- Before pushing, re-run any quick verification from `skills/testing.md` and note the results in the commit description or a nearby TODO.
- Use `skills/review.md` to confirm code clarity after each commit and describe any assumptions in the log.
