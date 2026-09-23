# VibeCheck Android

Android-first social party game with a viral sharing loop.

## Current M0 slice
- 4 game modes
- offline local questions
- quick voting loop
- deterministic results
- native result sharing
- no account/backend required

## Stack
- Kotlin
- Jetpack Compose
- Material 3
- JDK 17
- compileSdk / targetSdk 36
- minSdk 24

The architecture direction follows the Android Product references in `dbrckk/star-list`, especially `android/nowinandroid`. `airbnb/lottie-android` is reserved for later micro-animation work where it adds real UX value.

## Verification

With Gradle 8.11.1+ installed:

```bash
gradle :app:testDebugUnitTest
gradle :app:lintDebug
gradle :app:assembleDebug
```

## Monetization
Core play remains free. Ads will be inserted only at natural session boundaries. A non-consumable Play Billing product will permanently disable ads. Billing and ads are intentionally deferred until the core loop is stable.
