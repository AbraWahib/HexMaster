# AGENTS.md — HexMaster

Guidance for any AI coding agent (Claude Code, etc.) working in this repository.

## Project summary

HexMaster is an Android arcade game: a random color is displayed and the player
types its hex code as a guess. Three difficulties change the feedback and
scoring. Full design spec: `HexMaster_Architecture_Plan.md` in the repo root —
read it before making structural changes.

- **App display name:** HexMaster
- **Application ID / package:** `com.abra.hexmaster`

## Stack

- Kotlin, Jetpack Compose (Material 3), Compose Navigation
- MVVM: `ViewModel` + `StateFlow`, unidirectional UI state
- Manual DI via `di/AppContainer.kt` — do not add Hilt/Dagger without asking
- Jetpack DataStore (Preferences) for persistence — no Room/SQL, this app does
  not need a full database. Keys: `high_score_overall`, `high_score_easy`,
  `high_score_medium`, `high_score_hard`, `best_streak`, `sound_enabled`,
  `vibration_enabled` (see plan §3.6)
- **minSdk 24** (Android 7.0). targetSdk/compileSdk: check
  `gradle/libs.versions.toml` for the actual pinned number, don't guess.
- Theme: Material 3 default color scheme/typography for v1 — no custom neon
  palette or pixel font unless explicitly asked. Keep `ui/theme/` files
  structured as a single source of truth so a future palette swap is
  low-effort (don't hardcode M3 color/type values into individual screens).

## Non-negotiable architectural rules

1. **Game logic stays out of Composables.** `ColorGenerator`, `HexColorUtils`,
   `GuessEvaluator`, `ScoreCalculator`, and `GameEngine` are pure Kotlin
   (no Android/Compose imports) and must stay unit-testable without an
   emulator. If you're tempted to put grading/scoring math inside a
   `@Composable`, stop and put it in `core/` or `domain/` instead.
2. **State flows one way.** ViewModels expose a single `StateFlow<UiState>`
   per screen; Composables read state and emit events up (via lambdas), they
   never mutate ViewModel state directly.
3. **Difficulty-specific tuning constants live in `GameBalanceConfig.kt`.**
   Don't hardcode magic numbers (thresholds, timer length, base points) inline
   in the engine or UI — add/adjust them there so they stay discoverable.
4. **Keep Easy/Medium/Hard grading logic in `GuessEvaluator`, not duplicated
   per screen.** The Game screen should call one evaluator and render
   whichever feedback view (`DigitFeedbackRow` vs `SimilarityMeter`) matches
   the current difficulty — don't reimplement grading in the UI layer.
5. **New screens go through `Screen.kt` + `HexMasterNavGraph.kt`.** Don't wire
   ad-hoc navigation.
6. **Hex input is OTP-style: 6 single-character boxes**, auto-advancing focus,
   case-insensitive (normalize to uppercase), no `#` box. Submit is always
   tappable — an incomplete/invalid guess shows an inline error + horizontal
   shake and does **not** evaluate (no life lost, no round consumed).
   **Backspace navigation:** tapping backspace in a box (even if not empty)
   should move focus to the previous box.
   **Tries:** The player has 6 tries per round to get the correct color.
   A life is only lost if the 6th try is also incorrect. Success on any try
   (1-6) resets tries for the next round.
   **Reset behavior:** After an incorrect guess (if tries remain), the input
   boxes should be cleared immediately for the next attempt.
7. **Never make feedback color-only.** Easy mode digit boxes always render
   their arrow glyph alongside the background color; Medium/Hard similarity
   is always shown as a legible number, not just a colored gauge. Any new
   color-coded state (lives, pass/fail flashes, etc.) needs a paired
   text/icon cue. This is a hard requirement, not a nice-to-have — treat a
   PR that adds color-only feedback as incomplete.
8. **Do not fabricate audio or icon assets.** `res/raw/` sound files and the
   app launcher icon are user-supplied. Wire up the playback/display code
   and reference sensible filenames (e.g. `sfx_correct`, `sfx_wrong`,
   `sfx_tick`, `sfx_submit`) but leave actual asset creation to the user —
   use the Android Studio default launcher icon as a placeholder in the
   meantime, don't generate custom audio/icon files.
9. **Game Over → Retry opens a choice dialog**: "same difficulty" (instant
   restart) or "change difficulty" (→ Difficulty Select). Don't skip straight
   to a restart or straight to Difficulty Select.

## Testing expectations

- Any change to `core/` or `domain/` logic needs/updates a corresponding unit
  test in `src/test/...`. These are pure Kotlin tests — no Android instrumentation
  needed, they should run fast.
- Before considering a logic change done, run:
  ```
  ./gradlew test
  ```
- UI/Compose changes: a build + manual smoke check is acceptable day-to-day;
  add a Compose UI test under `src/androidTest/` only for flows worth locking
  in (e.g. full round submit flow), not for every small tweak.

## Style

- Standard Kotlin style (official codestyle). If ktlint/detekt config is added
  later, follow it; until then, keep formatting consistent with surrounding code.
- Prefer explicit, small, single-purpose composables in `ui/components/` over
  large monolithic screen files — screens should mostly *compose* components,
  not contain deeply nested layout logic.

## Things to check before assuming

- **Gradle/library versions** — read `gradle/libs.versions.toml`, don't guess.
- **Balance numbers** (lives, thresholds, timer length, scoring) — read
  `GameBalanceConfig.kt`, don't hardcode a remembered value from the plan doc;
  the plan doc is a starting point and may have been tuned since.
- **Screen list/navigation graph** — read `Screen.kt` before adding a new
  route, to match existing naming.

## Commit hygiene

- Keep commits scoped (e.g. "Add GuessEvaluator + unit tests" rather than
  bundling engine logic with UI polish).
- Don't commit generated build output or local `local.properties`.
