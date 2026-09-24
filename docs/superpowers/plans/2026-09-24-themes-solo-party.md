# Themes & Solo Party Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Improve VibeCheck UX with selectable visual themes and add a fully offline solo mode populated by simulated public-celebrity personas plus original fictional personas.

**Architecture:** Keep the existing Compose/GameViewModel game loop intact. Add a persisted appearance preference, a theme catalog, and a separate solo-party domain layer that converts selected simulated personas into game participants. Persona answers are deterministic from persona traits + question + session seed so solo works offline and remains replayable. Public-person responses must always be presented as fictional simulations, never factual statements or endorsements.

**Tech Stack:** Kotlin, Jetpack Compose Material 3, AndroidX lifecycle/SavedStateHandle, AndroidX DataStore Preferences for settings, existing local stores/CI. Prefer AndroidX/Compose APIs; add an external GitHub/library dependency only where it materially improves UX or maintainability.

**Spec:** User-approved direction in conversation: six selectable themes; two clear entry points (play together / solo); mixed solo catalog with real public celebrities and original characters; at least 30 public personas split 15 men/15 women; offline deterministic simulation.

## Global Constraints

- Preserve existing multiplayer behavior and challenge compatibility.
- Offline-first: solo gameplay must not require an API, account, network, or paid AI service.
- Minimum public-person catalog: 30 profiles, 15 men and 15 women.
- Also ship original fictional personas for greater variety.
- Public-person answers are explicitly fictional simulations inspired only by broad public persona archetypes; do not present private facts, sensitive attributes, political views, health claims, or actual quotations as simulated knowledge.
- Theme choice persists across launches.
- Initial themes: Premium Dark, Kawaii, Pop, Elegant, Street, Minimal.
- Theme labels are aesthetic styles, not gender restrictions; any user may select any theme.
- Keep accessibility contrast, touch targets, reduced-motion compatibility, and content descriptions.
- Use TDD for domain/state changes and Compose tests for critical user flows.
- Every independently testable task ends with tests/build verification and a small commit.

## Review Focus

- Corrupt/unknown stored theme value must safely fall back to Premium Dark.
- Solo session with too few selected personas must not start and must explain the minimum clearly.
- Same persona/question/seed must produce the same simulated answer; changing seed may vary ties without becoming random/non-reproducible.
- Public-person simulation UI must visibly label answers as fictional/simulated and never imply the celebrity actually said them.
- Switching theme during/after a session must not erase game state or player-group state.

---

### Task 1: Appearance domain and persistent theme setting

**Files:**
- Create: `app/src/main/java/com/vibecheck/app/ui/theme/VibeThemeStyle.kt`
- Create: `app/src/main/java/com/vibecheck/app/data/AppearanceStore.kt`
- Modify: `app/src/main/java/com/vibecheck/app/ui/theme/VibeTheme.kt`
- Modify: `app/src/main/java/com/vibecheck/app/ui/VibeCheckApp.kt`
- Create: `app/src/test/java/com/vibecheck/app/ui/theme/VibeThemeStyleTest.kt`

- [ ] Write failing tests for six stable theme IDs and unknown-value fallback.
- [ ] Run unit tests and confirm RED.
- [ ] Implement `VibeThemeStyle` catalog and safe parser.
- [ ] Implement DataStore-backed appearance persistence.
- [ ] Refactor `VibeCheckTheme(style=...)` and backdrop tokens so every theme supplies a complete Material color scheme and background treatment.
- [ ] Hydrate selected theme at app root without touching game state.
- [ ] Run unit tests, lint and compile; commit.

### Task 2: Settings UX and theme preview

**Files:**
- Create: `app/src/main/java/com/vibecheck/app/ui/screens/SettingsScreen.kt`
- Modify: `app/src/main/java/com/vibecheck/app/ui/screens/HomeScreen.kt`
- Modify: `app/src/main/java/com/vibecheck/app/ui/VibeCheckApp.kt`
- Modify/Create Compose tests under `app/src/androidTest/java/com/vibecheck/app/ui/screens/`

- [ ] Write failing Compose tests for opening Settings, seeing all six themes, selecting Kawaii, and returning with selection retained.
- [ ] Run instrumentation compile/tests to confirm RED.
- [ ] Add a compact Settings entry to Home.
- [ ] Build premium theme cards with live mini-preview, selected state, accessible labels and immediate application.
- [ ] Keep secondary settings visually subordinate to the two primary play actions.
- [ ] Run Compose tests/lint/build; commit.

### Task 3: Simplify Home into multiplayer vs solo entry points

**Files:**
- Modify: `app/src/main/java/com/vibecheck/app/ui/screens/HomeScreen.kt`
- Modify: `app/src/main/java/com/vibecheck/app/ui/VibeCheckApp.kt`
- Modify Compose tests.

- [ ] Write failing test requiring two dominant actions: `Jouer ensemble` and `Jouer en solo`.
- [ ] Confirm RED.
- [ ] Refactor Home hierarchy so mode/pack/intensity configuration is progressive disclosure rather than first-screen clutter.
- [ ] Preserve quick-start and leaderboard/premium access without competing visually with primary actions.
- [ ] Verify existing multiplayer navigation tests remain green; commit.

### Task 4: Persona domain model and safe catalog

**Files:**
- Create: `app/src/main/java/com/vibecheck/app/domain/solo/Persona.kt`
- Create: `app/src/main/java/com/vibecheck/app/domain/solo/PersonaTraits.kt`
- Create: `app/src/main/java/com/vibecheck/app/data/PersonaCatalog.kt`
- Create: `app/src/test/java/com/vibecheck/app/data/PersonaCatalogTest.kt`

- [ ] Write failing tests requiring >=40 total personas, >=30 public personas, exactly/at least 15 public men and 15 public women, unique IDs, and >=10 original personas.
- [ ] Confirm RED.
- [ ] Implement structured traits such as humor, boldness, empathy, sociability, competitiveness, romanticism, chaos and deliberation on bounded scales.
- [ ] Add 30 diverse public-celebrity personas and at least 10 originals; descriptions stay broad, playful and non-sensitive.
- [ ] Add metadata distinguishing `PUBLIC_SIMULATION` from `ORIGINAL`.
- [ ] Run tests; commit.

### Task 5: Deterministic offline persona answer engine

**Files:**
- Create: `app/src/main/java/com/vibecheck/app/domain/solo/PersonaAnswerEngine.kt`
- Create: `app/src/main/java/com/vibecheck/app/domain/solo/PersonaAnswer.kt`
- Create: `app/src/test/java/com/vibecheck/app/domain/solo/PersonaAnswerEngineTest.kt`

- [ ] Write failing tests for determinism, bounded scoring, different persona tendencies, tie handling and safe fallback for unknown question semantics.
- [ ] Confirm RED.
- [ ] Implement scoring from question tags/options + persona trait vector + stable seed hash.
- [ ] Never synthesize factual claims or quotes; engine chooses among game-provided options and may generate only short clearly fictional flavor reactions from a controlled template set.
- [ ] Ensure public persona results carry `isFictionalSimulation=true`.
- [ ] Run tests; commit.

### Task 6: Solo party selection UX

**Files:**
- Create: `app/src/main/java/com/vibecheck/app/ui/screens/SoloPartyScreen.kt`
- Create: `app/src/main/java/com/vibecheck/app/ui/state/SoloPartyViewModel.kt`
- Modify: `app/src/main/java/com/vibecheck/app/ui/VibeCheckApp.kt`
- Add unit and Compose tests.

- [ ] Write failing tests for browsing Public/Original, selecting 2–7 companions, auto-compose, deselecting, and minimum/maximum enforcement.
- [ ] Confirm RED.
- [ ] Implement searchable/filterable persona grid with lightweight local assets/monograms initially; do not ship unlicensed celebrity photography.
- [ ] Add `Composer automatiquement` that balances profiles by traits and optionally presentation mix.
- [ ] Show persistent disclaimer: public-person responses are fictional simulations for entertainment.
- [ ] Start solo session only when selection is valid.
- [ ] Run tests/lint/build; commit.

### Task 7: Integrate solo participants into existing game loop

**Files:**
- Modify: `app/src/main/java/com/vibecheck/app/ui/state/GameViewModel.kt`
- Modify: `app/src/main/java/com/vibecheck/app/ui/screens/GameScreen.kt`
- Modify: `app/src/main/java/com/vibecheck/app/ui/screens/ResultScreen.kt`
- Modify: `app/src/main/java/com/vibecheck/app/ui/VibeCheckApp.kt`
- Add/modify unit and Compose tests.

- [ ] Write failing end-to-end state tests: solo setup -> game -> simulated responses -> result -> replay -> home.
- [ ] Confirm RED.
- [ ] Introduce session participant type without breaking persisted human multiplayer sessions.
- [ ] Feed persona answers through `PersonaAnswerEngine` at the correct turn/reveal moment.
- [ ] Make simulated turns visually distinct and clearly labelled `Simulation fictive` for public personas.
- [ ] Preserve result scoring, replay and SavedState restoration.
- [ ] Run full test suite/lint/build; commit.

### Task 8: Premium UX pass for solo and themes

**Files:**
- Modify theme/screen files from Tasks 1–7.
- Add assets only when license/provenance is safe.

- [ ] Add restrained transitions, haptics and theme-specific decorative tokens using Compose/AndroidX first.
- [ ] Respect reduced-motion/system animation settings.
- [ ] Ensure Kawaii/Pop remain readable rather than juvenile clutter; Elegant/Premium remain distinctive rather than palette swaps.
- [ ] Test narrow Android screens and large font scaling.
- [ ] Run lint, unit tests, instrumentation compile/tests, debug APK and release AAB; commit.

### Task 9: MVP release verification

**Files:**
- Modify docs/README only if behavior changed from documented product.

- [ ] Run all unit tests.
- [ ] Run/compile all instrumentation tests available in CI.
- [ ] Run Android lint.
- [ ] Build debug APK.
- [ ] Build release AAB.
- [ ] Verify multiplayer regression path manually/test automation: onboarding -> together -> players -> game -> result -> replay.
- [ ] Verify solo path: home -> solo -> select personas -> game -> result -> replay.
- [ ] Verify all six themes survive app recreation.
- [ ] Verify no public-person screen implies actual quotes/opinions.
- [ ] Update README feature/status section and commit only after fresh verification evidence.
