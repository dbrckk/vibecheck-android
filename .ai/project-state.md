# Project state

## Product
VibeCheck is an Android-first social party game designed around short sessions and shareable results.

## Current milestone
M0 — runnable offline vertical slice.

## Implemented
- Kotlin Android application skeleton.
- Jetpack Compose single-activity UI.
- Four initial modes.
- Local question catalog.
- Deterministic vote/result engine.
- Native Android text sharing.
- Unit tests for result calculation.

## Next
1. Get CI green and fix compile/lint issues.
2. Add player setup instead of hard-coded demo names.
3. Improve game-state save/restore.
4. Generate shareable 9:16 result cards.
5. Add deep-link challenge format.
6. Only then add lifetime remove-ads billing and ad placements.

## Constraints
- Offline-first.
- No account required for core play.
- Keep infrastructure cost near zero.
- Free/open-source dependencies preferred.
- Freemium: ads + one-time lifetime remove-ads purchase.
