# VibeCheck V1 — Play Store release checklist

This checklist is the final gate after source CI is green. Never commit keystores, passwords, Play service-account credentials, or other signing secrets.

## 1. Source candidate

- [ ] Exact release SHA recorded.
- [ ] `gradle :app:testDebugUnitTest --stacktrace` passes.
- [ ] `gradle :app:compileDebugAndroidTestKotlin --stacktrace` passes.
- [ ] `gradle :app:lintDebug --stacktrace` passes.
- [ ] `gradle :app:assembleDebug --stacktrace` passes.
- [ ] `gradle :app:bundleRelease --stacktrace` passes.
- [ ] No signing material is tracked by Git.

## 2. Play signing

Use Google Play App Signing. Keep the upload key outside the repository.

Create an upload key once on a trusted machine if one does not already exist:

```bash
keytool -genkeypair -v \
  -keystore vibecheck-upload.jks \
  -alias vibecheck-upload \
  -keyalg RSA -keysize 2048 -validity 10000
```

Store the keystore and passwords in a password manager / secure backup. Do not place them in the repository.

For local signing, use an ignored `keystore.properties` file or Android Studio's Generate Signed Bundle flow. The final artifact submitted to Play must be an Android App Bundle (`.aab`).

## 3. Device smoke test

Test the exact candidate installed on a physical Android device:

- [ ] Fresh install opens without crash.
- [ ] Onboarding can be completed.
- [ ] Relaunch skips completed onboarding.
- [ ] Local player group can be created, edited, and survives relaunch.
- [ ] Who of us completes a full session and result screen.
- [ ] Most likely completes a full session and result screen.
- [ ] Red / Green completes a full session and result screen.
- [ ] Who knows me completes a full session and result screen.
- [ ] Solo Party can select personas and complete a session.
- [ ] Solo public-person simulation is clearly labelled fictional / entertainment.
- [ ] Solo result does not offer a real friend challenge.
- [ ] Solo share action is labelled as a simulation.
- [ ] Result image sharing opens Android's chooser and the receiving app can read the image.
- [ ] Friend challenge deep link opens VibeCheck and restores the intended challenge.
- [ ] Theme choice survives app restart.
- [ ] System reduced-motion / animation-disabled setting does not break navigation.
- [ ] Back navigation does not lose or corrupt an active session.
- [ ] Rotation / activity recreation does not corrupt an active session.
- [ ] Offline core play works with airplane mode enabled.

## 4. Billing

V1 is ad-free and does **not** expose the legacy `remove_ads_lifetime` purchase.

Release gate:

- [ ] `ReleaseConfig.adRemovalPurchaseEnabled == false`.
- [ ] No Premium / ad-removal purchase card is visible in the V1 UI.
- [ ] Google Play Billing is not initialized during normal V1 app startup.
- [ ] Play Console does not need an active `remove_ads_lifetime` product for V1.

If ads or paid entitlements are introduced later, re-enable billing deliberately and run a new billing, privacy and Data Safety review before publication.

## 5. Play Console declarations

- [ ] App name and default language configured.
- [ ] App category selected.
- [ ] Store short description completed.
- [ ] Store full description completed.
- [ ] High-resolution app icon uploaded.
- [ ] Feature graphic uploaded.
- [ ] Phone screenshots uploaded from the release candidate.
- [ ] Privacy policy URL supplied if required by the final SDK/data configuration.
- [ ] Data safety declaration matches the exact release artifact and SDKs.
- [ ] Ads declaration matches the exact release artifact.
- [ ] App access declaration completed.
- [ ] Content rating questionnaire completed accurately.
- [ ] Target audience / content declarations completed.
- [ ] Countries / regions and pricing configured.

## 6. Closed test and pre-launch

- [ ] Upload the signed AAB to an internal or closed test track first.
- [ ] Resolve Play Console blocking warnings/errors.
- [ ] Review Android pre-launch report for crashes, ANRs, rendering and accessibility issues.
- [ ] Test installation/update through Google Play on at least one physical device.
- [ ] Repeat the critical smoke path on the Play-delivered build.

## 7. Production gate

Production rollout is allowed only when:

1. source CI is green on the exact release SHA;
2. the signed AAB is accepted by Play;
3. the Play-delivered build passes the critical smoke test;
4. billing, declarations and store assets match what is actually shipped;
5. there are no unresolved release-blocking crashes, ANRs, policy issues, or data-safety mismatches.
