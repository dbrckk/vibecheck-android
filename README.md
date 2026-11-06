# VibeCheck Android

Android-first social party game with an offline-first core, local multiplayer, public-figure Solo Party and native sharing.

## Current release candidate

- Version: `1.2.1` (`versionCode 5`)
- Application ID: `com.dbrckk.vibecheck`
- compileSdk / targetSdk: 36
- minSdk: 24
- JDK: 17
- Google Play App Signing enabled
- Release builds use R8 and resource shrinking

## Product

- Four group modes: Who of us, Most likely, Red / Green and Who knows me.
- 2–8 player local group with normalized names and local persistence.
- Participative Solo Party using **public figures only**.
- Public-figure Solo answers are deterministic fictional entertainment simulations and are explicitly labelled as such in setup, gameplay, results and sharing.
- Local question catalog and seeded sessions.
- Native result-card sharing through a non-exported `FileProvider`.
- Versioned `vibecheck://challenge` links with strict parsing and legacy compatibility.
- Pastel tactile UI, selectable themes, ambient local soundtrack and reduced-motion support.
- No account or backend required for core play.

## Privacy and monetization posture

The current release:

- does not include advertising;
- does not include analytics or behavioral tracking;
- does not include Google Play Billing or an in-app purchase flow;
- does not request broad storage, contacts, location, microphone, camera or phone permissions;
- keeps player names, groups, preferences and statistics on-device;
- explicitly excludes app data from Android cloud backup and device-transfer backup;
- exposes the public privacy policy from Settings.

If ads, analytics, accounts, cloud services or purchases are introduced later, the privacy policy and Play Console declarations must be reviewed again before release.

## Verification gate

Every production candidate must pass on its **exact commit SHA**:

```bash
gradle :app:testDebugUnitTest --stacktrace
gradle :app:compileDebugAndroidTestKotlin --stacktrace
gradle :app:lintRelease --stacktrace
gradle :app:assembleDebug --stacktrace
gradle :app:bundleRelease --stacktrace
```

CI also:

- audits `releaseRuntimeClasspath` for forbidden ads, analytics and billing SDKs;
- verifies 16 KB ELF segment alignment in native libraries;
- executes Compose/instrumentation tests on an Android 16 / API 36 emulator;
- uploads the debug APK and exact unsigned release AAB.

A candidate is not production-ready until both the release verification job and emulator instrumentation job are green.

## Play Store release gate

1. CI is green on the exact release SHA.
2. Sign the exact CI AAB with the current Google Play upload key kept outside Git.
3. Verify the signed AAB certificate and SHA-256.
4. Upload that exact artifact to an internal or closed Play track.
5. Install the Play-delivered build on a physical device and smoke-test all group modes, Solo Party, sharing, challenge links, music/settings, process/activity recreation and offline play.
6. Review the Google Play pre-launch report and resolve blocking crashes, ANRs, rendering, accessibility or policy issues.
7. Promote the exact validated artifact to production.

Never commit keystores, signing passwords, service-account credentials or Play Console secrets.
