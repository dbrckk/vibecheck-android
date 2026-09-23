# Project state

## Product
VibeCheck is an Android-first social party game built around fast group sessions, shareable 9:16 results and friend challenges.

## Current milestone
M1 — stable offline vertical slice with polished core UX, reproducible challenges and lifetime Premium billing foundation.

## Implemented
- Kotlin + Jetpack Compose single-activity Android app.
- Four initial game modes with distinct visual identities.
- Local 96-question catalog and seeded randomized 8-question sessions.
- 2–8 player setup with normalized names and validation.
- RED_GREEN starts directly without unnecessary player setup.
- Game session state centralized in GameViewModel + SavedStateHandle.
- Deterministic vote/result engine with unit coverage.
- Lightweight native question/progress/result animations.
- Duplicate-answer tap protection and haptic answer feedback.
- Recoverable empty-question state instead of an index crash.
- Native 1080x1920 result-card rendering with per-mode palettes.
- Result PNG generation, compression and cache writing off the UI thread.
- Share preparation/error state and duplicate-share protection.
- Secure FileProvider + ClipData image sharing with cache pruning.
- Versioned challenge links carrying mode, target and session seed.
- Legacy challenge links remain readable.
- Incoming challenges reproduce the same question selection/order.
- Challenge replay preserves the seed; tied target score counts as success.
- Lifetime Premium BillingClient foundation with localized Play price.
- Existing purchases restored; acknowledgement retry cannot recurse tightly.
- GitHub Actions: unit tests, lint, debug APK build, release AAB build and artifacts.
- CI uses checkout/setup-java v5 and Gradle setup v4.
- Dedicated Home, PlayerSetup, Game and Result screens.
- Home, gameplay, result and shared cards use a coherent mode-specific visual system.

## Next
1. Improve player setup ergonomics and Android back-navigation behavior.
2. Add focused UI/instrumentation tests for the main play path.
3. Extract hardcoded user-facing strings for localization/accessibility.
4. Replace custom challenge URI with verified HTTPS App Links once a real domain exists.
5. Add ads/consent only after real AdMob identifiers exist; never ship placeholder IDs.
6. Prepare release signing, privacy policy, Data safety and Play Store release checklist.
7. Revisit challenge-score semantics per mode and evolve KNOWS_ME into a genuinely distinct game mechanic.

## Constraints
- Offline-first core play.
- No account required.
- Near-zero infrastructure cost.
- Prefer free/open-source dependencies.
- Monetization: carefully placed ads + one-time lifetime remove-ads purchase.
- Do not invent production domains, ad IDs, signing credentials or store configuration.
