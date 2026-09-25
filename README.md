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
- cleartext HTTP traffic disabled

## V1 privacy posture
The core game does not require an account or backend. Game state, theme preferences and solo-party state are local to the device. V1 does not request broad storage, contacts, location, microphone or camera permissions. Native sharing is initiated explicitly by the user. Any future advertising/analytics integration must update the Play Console Data safety declaration and this documentation before release.

## Verification gate

CI verifies every pull request with:

```bash
gradle :app:testDebugUnitTest --stacktrace
gradle :app:compileDebugAndroidTestKotlin --stacktrace
gradle :app:lintDebug --stacktrace
gradle :app:assembleDebug --stacktrace
gradle :app:bundleRelease --stacktrace
```

The workflow publishes the debug APK and an unsigned release AAB as CI artifacts. A release candidate is not considered verified until this complete gate has passed on its exact commit SHA.

## Play Store release gate
Before production publication, the remaining distribution work is deliberately kept outside source control:
1. configure the Play App Signing/upload key without committing keystore material;
2. build and verify a signed release AAB;
3. complete Play Console store listing, screenshots, content rating, data-safety and app-access declarations;
4. run the final device/pre-launch smoke test on the exact signed candidate.

Do not commit keystores, signing passwords, service-account credentials or Play Console secrets to this repository.

## Monetization
Core play remains free. Google Play Billing is available in the project for the planned non-consumable ad-removal purchase. Ads must only appear at natural session boundaries; monetization must not block the core game loop.
