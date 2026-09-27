# VibeCheck V1.1 UX, Audio & Public Solo Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Make VibeCheck more tactile and polished, add persistent lifecycle-safe ambient music, and restrict Solo Party to highly recognizable public-figure simulations.

**Architecture:** Keep the existing Compose/theme/view-model structure. Centralize tactile button treatment in a small UI component/policy, keep audio state in a local store plus a lifecycle-aware controller owned at app level, and make the public-only Solo invariant explicit in `PersonaCatalog`/`SoloPartyViewModel` so UI filtering cannot bypass it.

**Tech Stack:** Kotlin, Jetpack Compose Material 3, Android lifecycle/audio APIs, SharedPreferences/StateFlow, JUnit, Compose UI tests, Gradle.

**Spec:** `docs/superpowers/specs/2026-09-26-v1-1-ux-audio-public-solo.md`

## Global Constraints

- Work on `feat/v1-themes-solo-party`.
- Preserve the drawn pastel identity and existing theme system.
- Solo Party exposes only `PersonaKind.PUBLIC_SIMULATION`.
- Human player answers before public-figure simulations are revealed.
- Music is optional, local, low-volume by default, persistent, and lifecycle/audio-focus aware.
- Shipped audio must be legally redistributable in a commercial Play Store app with license metadata recorded when required.
- No TODOs/placeholders in the shipped path.

## Review Focus

- Previously persisted fictional Solo IDs: sanitize them rather than restoring inaccessible companions.
- Auto-compose edge seeds/counts: always return only unique public personas within 2..7 companions.
- Audio focus/background transitions: never create overlapping players or resume after the user disabled music.
- Volume boundary values: clamp persisted and UI values to 0..1.
- Press/reduced-motion interaction: controls remain obviously tappable without requiring animation.

---

### Task 1: Enforce a public-only Solo Party catalog

**Files:**
- Modify: `app/src/main/java/com/vibecheck/app/data/PersonaCatalog.kt`
- Modify: `app/src/main/java/com/vibecheck/app/ui/state/SoloPartyViewModel.kt`
- Modify: `app/src/main/java/com/vibecheck/app/ui/screens/SoloPartyScreen.kt`
- Test: `app/src/test/java/com/vibecheck/app/data/PersonaCatalogTest.kt`
- Test: `app/src/test/java/com/vibecheck/app/ui/state/SoloPartyViewModelTest.kt`
- Test: `app/src/androidTest/java/com/vibecheck/app/ui/screens/SoloPartyScreenTest.kt`

**Interfaces:**
- Produces: `PersonaCatalog.soloPublic: List<Persona>` as the only Solo selection source.
- Produces: `SoloPartyViewModel` state whose selected/visible personas are always public simulations.

- [ ] **Step 1: Write failing catalog/view-model tests** asserting `soloPublic` is non-empty, contains only `PUBLIC_SIMULATION`, contains no original IDs, restored fictional IDs are dropped, search returns only public figures, and `autoCompose(seed,count)` returns `count` unique public IDs deterministically.
- [ ] **Step 2: Run focused tests** with `./gradlew testDebugUnitTest --tests '*PersonaCatalogTest' --tests '*SoloPartyViewModelTest'`; expected failure on the new invariant/API.
- [ ] **Step 3: Implement `PersonaCatalog.soloPublic` and switch all Solo Party selection/search/toggle/auto-compose/restoration logic to it.** Remove kind-filter behavior that can expose originals; sanitize persisted IDs against `soloPublic`.
- [ ] **Step 4: Tighten Solo Party UI/copy** so no public/original filter is offered and the simulation disclaimer remains explicit.
- [ ] **Step 5: Run focused unit tests and relevant Compose tests**; expected PASS.
- [ ] **Step 6: Commit** with `feat: restrict solo party to public figures`.

### Task 2: Curate the recognizable public roster

**Files:**
- Modify: `app/src/main/java/com/vibecheck/app/data/PersonaCatalog.kt`
- Test: `app/src/test/java/com/vibecheck/app/data/PersonaCatalogTest.kt`

**Interfaces:**
- Consumes: `PersonaCatalog.soloPublic` from Task 1.
- Produces: a stable 30-40-person public roster with unique IDs/names and complete trait data.

- [ ] **Step 1: Add failing roster-contract tests** asserting size is within 30..40, IDs/display names are unique, every entry is public simulation, and a small fixed cross-category recognition baseline remains present.
- [ ] **Step 2: Run `./gradlew testDebugUnitTest --tests '*PersonaCatalogTest'`**; expected FAIL until roster meets contract.
- [ ] **Step 3: Curate the catalog** by removing all fictional originals from the Solo-facing roster and adding only broadly recognizable global entertainment/sports figures needed to reach the target range; keep archetypes clearly framed as game simulations rather than factual personality claims.
- [ ] **Step 4: Run focused tests**; expected PASS.
- [ ] **Step 5: Commit** with `content: curate solo public figure roster`.

### Task 3: Introduce consistent tactile controls

**Files:**
- Create: `app/src/main/java/com/vibecheck/app/ui/components/VibeActionSurface.kt`
- Modify: `app/src/main/java/com/vibecheck/app/ui/screens/HomeScreen.kt`
- Modify: `app/src/main/java/com/vibecheck/app/ui/screens/SettingsScreen.kt`
- Modify: `app/src/main/java/com/vibecheck/app/ui/screens/SoloPartyScreen.kt`
- Test: `app/src/androidTest/java/com/vibecheck/app/ui/screens/HomePrimaryActionsTest.kt`
- Test: `app/src/androidTest/java/com/vibecheck/app/ui/screens/SettingsScreenTest.kt`
- Test: `app/src/androidTest/java/com/vibecheck/app/ui/screens/SoloPartyScreenTest.kt`

**Interfaces:**
- Produces: `@Composable VibeActionSurface(...)` with clickable semantics, >=48dp minimum touch height, configurable accent/emphasis, elevation/border, and pressed visual state.

- [ ] **Step 1: Add failing Compose assertions** for click actions/roles, minimum practical touch sizing, enabled/disabled semantics, and Solo/home/settings primary actions remaining discoverable.
- [ ] **Step 2: Run connected Compose tests when a target exists**; otherwise compile androidTest and record that device execution remains pending.
- [ ] **Step 3: Implement `VibeActionSurface`** using existing colors/shapes, a clear border/elevation and pressed-state scale/elevation that degrades correctly when motion duration is zero.
- [ ] **Step 4: Migrate primary home cards, mode cards, Solo CTA/selection actions, and settings theme choices** without altering navigation behavior.
- [ ] **Step 5: Re-run Compose tests/compile**; expected PASS/clean compilation.
- [ ] **Step 6: Commit** with `feat: make primary actions visibly tactile`.

### Task 4: Add persistent music preferences

**Files:**
- Create: `app/src/main/java/com/vibecheck/app/data/AudioPreferencesStore.kt`
- Create: `app/src/test/java/com/vibecheck/app/data/AudioPreferencesStoreContractTest.kt`
- Modify: `app/src/main/java/com/vibecheck/app/ui/screens/SettingsScreen.kt`
- Modify: `app/src/main/java/com/vibecheck/app/ui/VibeCheckApp.kt`
- Test: `app/src/androidTest/java/com/vibecheck/app/ui/screens/SettingsScreenTest.kt`

**Interfaces:**
- Produces: `musicEnabled: StateFlow<Boolean>`, `musicVolume: StateFlow<Float>`, `setMusicEnabled(Boolean)`, `setMusicVolume(Float)`.
- Default: enabled; default volume: `0.18f`; volume always clamped to `0f..1f`.

- [ ] **Step 1: Add failing contract tests** for defaults, persistence keys, and volume clamping.
- [ ] **Step 2: Run `./gradlew testDebugUnitTest --tests '*AudioPreferencesStoreContractTest'`**; expected FAIL because store does not exist.
- [ ] **Step 3: Implement `AudioPreferencesStore`** following the local store patterns already used by appearance/onboarding.
- [ ] **Step 4: Add Settings music switch and volume slider** with localized labels and accessibility semantics; disable slider interaction when music is off.
- [ ] **Step 5: Run focused unit tests and compile relevant Compose tests**; expected PASS.
- [ ] **Step 6: Commit** with `feat: add persistent music settings`.

### Task 5: Add lifecycle-safe ambient playback

**Files:**
- Create: `app/src/main/java/com/vibecheck/app/audio/AmbientMusicController.kt`
- Create: `app/src/main/res/raw/vibecheck_pastel_ambient.*`
- Create: `docs/audio/vibecheck-pastel-ambient-license.md`
- Modify: `app/src/main/java/com/vibecheck/app/ui/VibeCheckApp.kt`
- Test: `app/src/test/java/com/vibecheck/app/audio/AmbientMusicPolicyTest.kt`

**Interfaces:**
- Produces: controller operations `start()`, `pause()`, `setEnabled(Boolean)`, `setVolume(Float)`, `close()`; internally requests/abandons Android audio focus and loops one local soundtrack.
- Consumes: `AudioPreferencesStore` state from Task 4.

- [ ] **Step 1: Write failing policy tests** for enabled/background/focus state transitions, no resume while disabled, no duplicate-start transition, and 0..1 volume clamping.
- [ ] **Step 2: Run `./gradlew testDebugUnitTest --tests '*AmbientMusicPolicyTest'`**; expected FAIL because policy/controller does not exist.
- [ ] **Step 3: Implement the testable playback policy plus `AmbientMusicController`** using Android audio focus and one looped local player; keep Android objects out of the pure policy tests.
- [ ] **Step 4: Add the final redistributable instrumental asset** and its source/license record. Do not commit an unlicensed or placeholder track.
- [ ] **Step 5: Own the controller in `VibeCheckApp`** with lifecycle observation and preference collection; release it exactly once on disposal.
- [ ] **Step 6: Run focused tests plus `./gradlew lintDebug assembleDebug`**; expected PASS.
- [ ] **Step 7: Commit** with `feat: add pastel ambient soundtrack`.

### Task 6: Polish Solo wording and complete regression verification

**Files:**
- Modify: `app/src/main/java/com/vibecheck/app/ui/screens/HomeScreen.kt`
- Modify: `app/src/main/java/com/vibecheck/app/ui/VibeCheckApp.kt`
- Modify tests only where copy/semantics contracts changed.

**Interfaces:**
- Consumes all prior tasks; produces the release-candidate V1.1 branch state.

- [ ] **Step 1: Add/update tests** pinning player-first Solo copy and explicit fictional-simulation disclosure after the player's answer.
- [ ] **Step 2: Replace remaining “fictional group/characters” wording** with concise public-figure simulation wording in FR/EN while retaining the disclaimer.
- [ ] **Step 3: Run `./gradlew testDebugUnitTest`**; expected PASS.
- [ ] **Step 4: Run `./gradlew lintDebug assembleDebug`**; expected PASS with an installable debug APK.
- [ ] **Step 5: Run/compile `connectedDebugAndroidTest` as environment permits** and inspect failures before proceeding.
- [ ] **Step 6: Inspect branch diff for placeholders, accidental fictional Solo exposure, duplicate audio ownership, and unrelated churn.**
- [ ] **Step 7: Commit** with `fix: polish v1.1 solo and ux copy`.
