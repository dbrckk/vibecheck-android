# VibeCheck V1.1 UX, Audio & Public Solo Spec

## Goal
Ship a more tactile, coherent VibeCheck experience: clearly clickable controls, calm high-quality background music matching the default drawn pastel identity, and a Solo Party made exclusively of globally recognizable public figures while keeping the human player active in every round.

## UX
- Preserve the existing drawn pastel visual identity and current theme system.
- Primary actions must read immediately as tappable controls through stronger elevation, border/contrast, adequate padding, and a visible pressed state.
- Keep touch targets at least 48dp where practical.
- Apply the interaction treatment consistently to home entry cards, mode cards, important CTA buttons, settings choices, and Solo Party controls without redesigning unrelated flows.
- Keep reduced-motion behavior compatible with the existing MotionPolicy.
- Replace Solo copy referring to a generic/fictitious group with public-figure simulation wording.

## Audio
- Add an app-owned ambient music controller rather than coupling playback directly to screen composables.
- Default soundtrack direction: gentle instrumental lo-fi/acoustic, warm and playful, no vocals, suitable for looping behind a party game and matching PASTEL_DRAWN.
- Playback must be optional, low-volume by default, loop cleanly, pause when the app loses foreground/audio focus, and resume only when appropriate.
- Settings expose a music enabled toggle and volume control. Preferences persist locally.
- No network streaming or account dependency.
- Any shipped audio asset must have a license that permits redistribution in a commercial Play Store app; record attribution/license metadata in the repository when required.

## Solo Party
- The selectable Solo Party catalog contains only PersonaKind.PUBLIC_SIMULATION entries.
- Remove original fictional personas from Solo Party discovery, search, selection, auto-compose, and restored selections.
- Auto-compose chooses only public simulations and remains deterministic for a supplied seed.
- Keep the current player-first round flow: the human chooses an answer before simulated public-figure responses are revealed.
- Public-figure answers remain fictional simulations and must never be presented as genuine statements, beliefs, endorsements, or quotations.
- Prefer roughly 30-40 internationally recognizable entertainment/sports public figures rather than padding the catalog with obscure names.
- Existing public figures may remain when broadly recognizable; add sports/global figures only when useful for diversity of recognition.

## Reliability / release constraints
- Work on `feat/v1-themes-solo-party` and preserve existing architecture where practical.
- TDD for behavior changes: add or tighten tests before implementation.
- No TODOs/placeholders in the shipped path.
- Run focused unit tests after each behavior slice, then the full unit suite, lint, and debug build before considering V1.1 complete.
- Instrumented Compose tests should be updated for semantics/UX behavior; run them when an Android test target is available.
