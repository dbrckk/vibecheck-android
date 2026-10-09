# VibeCheck agent instructions

## Shared policy — 88 validated development rules

- Official [88-rule catalog](https://github.com/dbrckk/repo-standards/blob/db2f86657ada74a0561e07189f9942d6b66ebb4a/standards/88-rules.md), [agent skill](https://github.com/dbrckk/repo-standards/blob/db2f86657ada74a0561e07189f9942d6b66ebb4a/skills/repo-excellence-88/SKILL.md) and [educational wiki](https://github.com/dbrckk/repo-standards/blob/db2f86657ada74a0561e07189f9942d6b66ebb4a/docs/WIKI-88.md).
- Apply relevant foundational rules; activate conditional rules only when applicable to this repository.
- **Do not create new unit tests.** Keep existing tests intact; prefer functional/integration validation, lint, build, and real smoke checks as appropriate. Report what actually ran.
- Inspect the current branch, changes, code, README and project-specific instructions before editing. Prefer the smallest complete and reversible change.
- Update project-state documentation and educational notes when warranted; capture proven reusable methods as skills.
- Priority: reliability > real functionality > security > architecture > performance > interface > secondary features.
- This is a **policy-only adoption**: no CI workflow, test suite, or runtime configuration is changed. Do not override local architecture or higher-priority instructions.

This repository follows the conventions from `dbrckk/repo-standards`.

## Reading order
1. `.ai/project-state.md`
2. Relevant source/test files only.
3. Generated `.ai/*` maps when the repo-standards workflow has populated them.

## Priorities
1. Keep the Android app buildable.
2. Keep the complete game loop usable before adding secondary features.
3. Verify deterministic domain logic with functional/integration checks when relevant; do not create new unit tests.
4. Preserve offline-first behavior.
5. Avoid unnecessary backend dependencies.
6. Ads must never block core play.
