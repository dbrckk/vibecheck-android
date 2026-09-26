# VibeCheck V1 Release Readiness Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Produce a first genuinely functional, installable and presentation-ready Android V1 with premium handcrafted branding and no known P0/P1 flow blockers.

**Architecture:** Preserve the existing single-activity Jetpack Compose architecture and SavedState/DataStore state management. Harden existing flows instead of expanding scope. Branding uses Android adaptive launcher resources with a safe legacy fallback; functional work remains covered by unit/instrumentation tests and the existing GitHub Actions release pipeline.

**Tech Stack:** Kotlin, Jetpack Compose Material 3, Android SDK 36/minSdk 24, DataStore, Play Billing, JUnit4, Compose UI tests, GitHub Actions.

**Spec:** Approved in-chat V1 release-readiness design, 2026-09-25.

## Global Constraints

- Reliability > working product > security > architecture > performance > UI > secondary features.
- No new secondary features until V1 blockers are cleared.
- Preserve existing architecture and current Solo/multiplayer behavior unless fixing a verified defect.
- Premium AAA target means coherent hierarchy, deliberate copy, touch/accessibility correctness and handcrafted branding; it does not justify unnecessary dependencies.
- Launcher icon direction: approved handcrafted deep navy/ink palette with warm cream, muted coral, soft teal, dusty lavender and restrained gold accent; no neon/AI-looking treatment.
- CI gate: unit tests, instrumentation compilation, lint, debug APK and release AAB must all pass.

## Review Focus

- Install/launch reliability on Android 7+ and modern adaptive-icon launchers.
- No dead ends in onboarding, Home, multiplayer, Solo, Settings, Premium, results/replay/back.
- Solo simulated personas never contaminate real-player stats/challenge flows.
- Theme and premium state survive activity recreation.
- Billing failures degrade safely and never falsely grant entitlement.
- Accessibility labels, minimum touch targets and text overflow on compact screens.

## Task 1 — Release inventory and blocker matrix

- [ ] Inspect manifest, Gradle, resources, all top-level screens, ViewModel state transitions, billing, persistence and deep-link handling.
- [ ] Classify findings P0/P1/P2 and record only reproducible issues.
- [ ] Add focused failing tests for each P0/P1 behavior before fixes.
- [ ] Commit the audit/test baseline.

## Task 2 — Production launcher branding

- [ ] Add an automated resource-level test/check that required launcher/adaptive-icon resources exist.
- [ ] Prepare approved artwork as Android-safe foreground/background assets without changing its handcrafted palette.
- [ ] Add `mipmap-anydpi-v26` adaptive icon XML and round icon wiring, plus density-compatible fallback resources.
- [ ] Ensure the foreground safe zone remains legible under circle, squircle and rounded-square masks.
- [ ] Build debug APK and inspect resource merge/aapt output.
- [ ] Commit launcher branding.

## Task 3 — Core navigation hardening

- [ ] Cover Home → multiplayer → game → result → replay/home with state-transition tests.
- [ ] Cover Home → Solo → casting → game → result → replay/home with state-transition tests.
- [ ] Verify back/exit actions cannot leave stale session flags or invalid selected personas/players.
- [ ] Fix only observed failures and rerun tests.
- [ ] Commit core-flow hardening.

## Task 4 — Persistence and lifecycle

- [ ] Test selected theme persistence and restoration.
- [ ] Test session-critical SavedStateHandle restoration where appropriate.
- [ ] Verify onboarding, premium entitlement and local stats stores fail safely on missing/corrupt/default state.
- [ ] Fix lifecycle/persistence defects.
- [ ] Commit persistence hardening.

## Task 5 — Billing and premium safety

- [ ] Audit BillingClient connection, product lookup, purchase acknowledgement, restore/reconnect and error states.
- [ ] Add tests around pure entitlement/error policy where testable without Play services.
- [ ] Ensure purchase UI cannot claim success before verified PURCHASED state.
- [ ] Ensure unavailable billing leaves the free app usable.
- [ ] Commit billing hardening.

## Task 6 — UX/accessibility premium pass

- [ ] Audit all interactive elements for clear affordance and >=48dp effective touch target where practical.
- [ ] Add content descriptions/semantics where icons alone communicate actions.
- [ ] Audit compact-screen text overflow and scrollability.
- [ ] Harmonize Solo casting/result copy and selected-state hierarchy with the approved premium direction.
- [ ] Keep theme variants coherent rather than forcing one palette on all users.
- [ ] Commit UX/accessibility pass.

## Task 7 — Release metadata and privacy/security sanity

- [ ] Verify exported components, deep-link scope, FileProvider paths, cleartext policy and backup policy.
- [ ] Verify no secrets/tokens/debug endpoints are committed.
- [ ] Verify version metadata and release minification configuration.
- [ ] Record Play Console items that cannot be completed in-repo (signing key, store listing, privacy/data-safety declarations) as explicit external release prerequisites.
- [ ] Commit release documentation/config fixes.

## Task 8 — Final verification

- [ ] Run/observe unit tests.
- [ ] Run/observe instrumentation compilation/tests available in CI.
- [ ] Run lint.
- [ ] Build debug APK.
- [ ] Build release AAB.
- [ ] Confirm artifacts are uploaded by CI.
- [ ] Perform final branch review for regressions and unresolved P0/P1 issues.
- [ ] Update project state with exact V1 status and remaining external prerequisites.
