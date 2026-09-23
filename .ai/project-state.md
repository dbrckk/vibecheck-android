# Project state

## Product
VibeCheck is an Android-first social party game designed around short sessions and shareable results.

## Current milestone
M0 — runnable offline vertical slice with real group setup.

## Implemented
- Kotlin Android application skeleton.
- Jetpack Compose single-activity UI.
- Four initial modes.
- Local question catalog with 48 starter prompts.
- Seeded randomized 8-question sessions that survive activity recreation.
- Deterministic vote/result engine.
- Native Android result sharing.
- Generated 1080x1920 PNG result cards.
- Secure FileProvider-based image sharing from app cache.
- Backend-free challenge links with vibecheck://challenge.
- Incoming challenges open directly into the selected mode.
- Native challenge sharing with a score-to-beat link.
- Real player setup flow.
- 2 to 8 players with normalized names.
- Duplicate, blank and oversized-name validation.
- Unit tests for result calculation and player rules.
- GitHub Actions verification: unit tests, lint and debug APK.

## Next
1. Keep CI green after player-flow changes.
2. Refine challenge UX and prepare verified HTTPS App Links when a public domain exists.
3. Refine result-card visual quality and variants.
4. Split the growing UI into feature files/ViewModel before adding monetization.
5. Only then add lifetime remove-ads billing and ad placements.

## Constraints
- Offline-first.
- No account required for core play.
- Keep infrastructure cost near zero.
- Free/open-source dependencies preferred.
- Freemium: ads + one-time lifetime remove-ads purchase.
