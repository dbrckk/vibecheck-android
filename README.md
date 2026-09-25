# VibeCheck Android

Android-first social party game with an offline-first core and native sharing.

## V1 feature set
- local multiplayer party flow
- offline Solo Party with selectable simulated personas and original fictional characters
- deterministic solo answers: same persona + question + seed produces the same result
- public-person answers are explicitly labelled fictional simulations for entertainment
- 6 selectable visual themes: Premium Dark, Kawaii, Pop, Elegant, Street and Minimal
- persistent appearance and solo-session state
- native result sharing and challenge deep links
- no account, backend or paid AI service required for core play
- reduced-motion support follows Android animation settings

## Android baseline
- Kotlin + Jetpack Compose + Material 3
- JDK 17
- compileSdk / targetSdk 36
- minSdk 24
- V1 version: `1.0.0` (`versionCode 1`)
- release builds enable code minification and resource shrinking
- Android backup disabled for the V1 offline data model

## Verification gate

CI verifies every pull request with:

```bash
gradle :app:testDebugUnitTest --stacktrace
gradle :app:compileDebugAndroidTestKotlin --stacktrace
gradle :app:lintDebug --stacktrace
gradle :app:assembleDebug --stacktrace
gradle :app:bundleRelease --stacktrace
```

The workflow publishes the debug APK and an unsigned release AAB as CI artifacts. A Play Store upload still requires the final app signing configuration and Play Console listing/policy assets.

## Monetization
Core play remains free. Google Play Billing is available in the project for the planned non-consumable ad-removal purchase. Ads must only appear at natural session boundaries; monetization must not block the core game loop.
