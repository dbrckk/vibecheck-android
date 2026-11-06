# Project state

## Product
VibeCheck is an Android-first social party game built around fast group sessions, shareable 9:16 results, friend challenges and an offline public-figure Solo Party.

## Current milestone
M1.2.1 — production hardening for Google Play.

## Implemented
- Kotlin + Jetpack Compose single-activity Android app.
- Four group game modes with distinct visual identities.
- Participative Solo Party using public-figure simulations only.
- Solo disclosures are visible in setup, gameplay, results and share copy; simulated answers are explicitly fictional entertainment and not real opinions.
- Offline-first core play with no account requirement.
- Local 96-question catalog and seeded randomized sessions.
- 2–8 player setup with normalized names and persisted local groups.
- Game session state centralized in GameViewModel + SavedStateHandle.
- Deterministic vote/result engine and existing JVM/instrumentation coverage.
- Native 1080x1920 result-card rendering and Android share flow through a non-exported FileProvider.
- Versioned custom-scheme challenge links with strict parsing and legacy compatibility.
- Pastel tactile UI, ambient local soundtrack, reduced-motion handling and safe-area-aware screens.
- No advertising, analytics or billing SDK in the current release.
- Local preferences, groups and statistics remain on-device; Android cloud backup and device-transfer backup rules explicitly exclude app data.
- targetSdk/compileSdk 36; minSdk 24.
- Release builds use R8/resource shrinking.
- GitHub Actions runs existing unit tests, instrumentation-test compilation, release lint, runtime-SDK audit, debug APK and release AAB builds, then executes the Compose/instrumentation suite on an Android 16 / API 36 emulator.
- Google Play App Signing is enabled; the recovered upload key is stored outside Git.

## Production gate
1. CI must be green on the exact release SHA.
2. Sign the exact CI AAB with the current Google Play upload key.
3. Verify the signed bundle certificate/hash locally.
4. Upload to an internal or closed Play track first.
5. Install the Play-delivered build on a physical device and smoke-test Home, all group modes, Solo Party, sharing, challenge links, music/settings, rotation/recreation and offline play.
6. Review Play pre-launch results and resolve any blocking crash, ANR, rendering, accessibility or policy issue.
7. Promote the exact validated artifact to production.

## Production hardening applied
- CI concurrency is branch-scoped correctly.
- AndroidX Test runner is explicitly configured with a deterministic French test locale.
- Home surfaces the persisted local group leader and restores access to the leaderboard.

## Remaining non-blocking improvements
1. Replace the custom `vibecheck://` challenge URI with verified HTTPS App Links once a controlled domain and the Play app-signing certificate association are available.
2. Move remaining hardcoded user-facing copy into Android resources without changing current FR/EN behavior.
3. Expand device-level accessibility validation, especially font scaling and TalkBack.
4. Add monetization only as a separate reviewed milestone; never ship placeholder ad or billing configuration.

## Constraints
- Offline-first core play.
- No account required.
- Near-zero infrastructure cost.
- Prefer free/open-source dependencies.
- Reliability > real functionality > security > architecture > performance > interface > secondary features.
