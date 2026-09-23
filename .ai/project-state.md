# Project state

## Product
VibeCheck is an Android-first social party game designed around short sessions and shareable results.

## Current milestone
M0 — runnable offline vertical slice with real group setup.

## Implemented
- Kotlin Android application skeleton.
- Jetpack Compose single-activity UI.
- Four initial modes.
- Local question catalog.
- Deterministic vote/result engine.
- Native Android result sharing.
- Generated 1080x1920 PNG result cards.
- Secure FileProvider-based image sharing from app cache.
- Real player setup flow.
- 2 to 8 players with normalized names.
- Duplicate, blank and oversized-name validation.
- Unit tests for result calculation and player rules.
- GitHub Actions verification: unit tests, lint and debug APK.

## Next
1. Keep CI green after player-flow changes.
2. Add deep-link challenge format.
3. Expand question content and randomized sessions.
4. Refine result-card visual quality and variants.
5. Only then add lifetime remove-ads billing and ad placements.

## Constraints
- Offline-first.
- No account required for core play.
- Keep infrastructure cost near zero.
- Free/open-source dependencies preferred.
- Freemium: ads + one-time lifetime remove-ads purchase.
