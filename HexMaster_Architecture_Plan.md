# HexMaster — Architecture & Implementation Plan

## 1. Overview

HexMaster is an arcade-style color-guessing game for Android. A random color is
shown; the player types its hex code. Feedback and scoring vary by difficulty:

| Difficulty | Feedback | Timer | Notes |
|---|---|---|---|
| Easy | Per-digit color (green/yellow/red) + directional arrows | None | Most forgiving, most informative |
| Medium | Overall similarity % | None | No timer, less granular feedback |
| Hard | Overall similarity % | Fixed per-round countdown | Same grading as Medium, adds pressure |

**Game loop:** Endless with 3 lives. Each round gives the player **6 tries** to match the color. Success on any try (1-6) scores points and advances to the next round. Failure on the 6th try → lose a life. After an incorrect guess (tries remaining), the input is cleared for the next attempt. 0 lives → Game Over.

**App identity:** display name `HexMaster`, application ID `com.abra.hexmaster`.

---

## 2. Tech Stack

- **Language:** Kotlin
- **UI:** Jetpack Compose + Material 3 (default M3 theme/typography/color scheme
  to start — no custom pixel font or bespoke palette for v1; swap in `ui/theme/`
  later if desired)
- **Architecture:** MVVM (`ViewModel` + `StateFlow` + unidirectional UI state)
- **Navigation:** Compose Navigation (single-Activity app)
- **DI:** Manual DI via a lightweight `ServiceLocator` / `AppContainer` object
  (Hilt is overkill for this app's size — easy to add later if it grows)
- **Persistence:** Jetpack DataStore (Preferences) — see §3.6 for exact keys
- **Testing:** JUnit + Turbine (Flow testing) for ViewModels; pure unit tests for
  game-logic classes (no Android dependency needed there)
- **Build config:** Gradle Kotlin DSL + a version catalog (`libs.versions.toml`)
- **minSdk:** 24 (Android 7.0) · **targetSdk/compileSdk:** latest stable at
  build time (confirm actual number against `libs.versions.toml` once created
  — don't hardcode a remembered number)

---

## 3. Core Game Logic

### 3.1 Color generation
`ColorGenerator` produces a random `HexColor` (backing RGB int triple 0–255 each).
Kept difficulty-agnostic — same generator for all modes.

### 3.2 Hex/RGB utilities
`HexColorUtils`:
- `hexToRgb(hex: String): Triple<Int,Int,Int>` (with validation)
- `rgbToHex(r,g,b): String`
- `isValidHex(input: String): Boolean` (6 hex chars, optional `#`)

### 3.3 Guess evaluation
`GuessEvaluator` is the heart of the grading system, and is pure logic
(no Android/Compose deps → fully unit-testable).

**Per-digit (Easy mode):**
```
digitValue = 0..15 (one hex nibble)
diff = |guessDigit - answerDigit|

color:
  diff == 0        -> GREEN
  diff in 1..2      -> YELLOW
  diff >= 3         -> RED

arrow:
  diff == 0                -> NONE
  diff in 1..7 (guess<ans) -> UP (single)
  diff in 1..7 (guess>ans) -> DOWN (single)
  diff >= 8 (guess<ans)    -> UP_UP (double)
  diff >= 8 (guess>ans)    -> DOWN_DOWN (double)
```
Output: `List<DigitFeedback>` (6 entries: color + arrow per digit).

**Overall similarity (Medium/Hard mode):**
```
channelDiff = |guessR-ansR| + |guessG-ansG| + |guessB-ansB|   // 0..765
similarityPercent = 100 - (channelDiff / 765.0 * 100)
```
Output: `Int` (0–100).

**Pass/fail threshold** (used to decide life loss), tunable constants:
- Easy: pass if no digit is RED
- Medium/Hard: pass if `similarityPercent >= 70`

> These thresholds are a starting point — easy to expose as difficulty-tuning
> constants in one file (`GameBalanceConfig.kt`) if you want to rebalance later.

### 3.3.1 Input handling & validation

- **Input UI:** OTP-verification style — 6 individual single-character boxes
  (one per hex digit), auto-advance focus to the next box as each character is
  typed. **Backspace navigation:** tapping backspace moves focus to the
  previous box, allowing quick corrections. Accepts `0-9`/`A-F` (case-insensitive,
  normalize to uppercase for display and comparison). No `#` box — the prefix
  isn't part of input at all.
- **Submit is always tappable.** If fewer than 6 boxes are filled, or a filled
  box somehow holds an invalid char (shouldn't happen if the input filters
  keystrokes, but treat defensively): on Submit, show an inline error state
  and **do not evaluate the guess** — no life lost, no round consumed, player
  just fixes the input and submits again.
- **Invalid submit feedback:** a simple horizontal shake animation on the
  input row (small translationX oscillation, ~300–400ms) plus the inline
  error text/border color. No sound/haptic requirement here specifically —
  reuse whatever the "wrong guess" cue is if it fits, but shake + text is the
  minimum.

### 3.3.2 Accessibility — never color-only

Feedback must never rely on color alone (colorblind-safe requirement):
- **Easy mode:** each digit box always shows its arrow glyph (↑ / ↑↑ / ↓ / ↓↓ /
  none) in addition to the background color — the arrow (or its absence) is
  sufficient on its own to read correctness even without color.
- **Medium/Hard mode:** the similarity is always shown as a **numeric
  percentage**, not just a colored gauge/bar. If a color gradient is used on
  the meter, the number must remain the primary legible element.
- **Lives/streak/pass-fail states:** pair any color-coded state (e.g. a red
  "life lost" flash) with a text or icon cue, not color changes alone.

### 3.4 Scoring
`ScoreCalculator`:
```
basePoints = when(difficulty) { EASY -> 100; MEDIUM -> 150; HARD -> 200 }
accuracyFraction = similarityPercent / 100.0   // (Easy derives an equivalent
                                                 // fraction from digit grades)
speedBonus = if (timed) (remainingTime / totalTime) * 0.5 + 1.0 else 1.0

roundScore = (basePoints * accuracyFraction * speedBonus).roundToInt()
```

### 3.5 Round/session state
`GameEngine` (or `GameSessionManager`) owns:
- current answer color
- lives remaining (starts at 3)
- score (running total)
- current streak + best streak this session
- round index
- difficulty
- timer state (Hard mode only)

Exposes a single `StateFlow<GameUiState>` the ViewModel/Composables observe.

### 3.6 Persistence (DataStore Preferences)

Keys needed in `SettingsDataStore`:
- `high_score_overall: Int`
- `high_score_easy: Int`, `high_score_medium: Int`, `high_score_hard: Int`
- `best_streak: Int` (overall best streak across sessions)
- `sound_enabled: Boolean`
- `vibration_enabled: Boolean`

On game over: compare session score against both the overall high score and
the current difficulty's high score, update whichever (or both) were beaten,
and surface a "new high score" banner accordingly on the Game Over screen.

---

## 4. File Structure

```
app/
 └─ src/
     ├─ main/
     │   ├─ java/com/abra/hexmaster/
     │   │   ├─ HexMasterApplication.kt
     │   │   ├─ MainActivity.kt
     │   │   │
     │   │   ├─ di/
     │   │   │   └─ AppContainer.kt              // manual DI holder
     │   │   │
     │   │   ├─ core/
     │   │   │   ├─ color/
     │   │   │   │   ├─ ColorGenerator.kt
     │   │   │   │   └─ HexColorUtils.kt
     │   │   │   ├─ scoring/
     │   │   │   │   ├─ GuessEvaluator.kt
     │   │   │   │   └─ ScoreCalculator.kt
     │   │   │   └─ GameBalanceConfig.kt          // thresholds, timer length, base points
     │   │   │
     │   │   ├─ data/
     │   │   │   ├─ model/
     │   │   │   │   ├─ Difficulty.kt             // enum: EASY, MEDIUM, HARD
     │   │   │   │   ├─ DigitFeedback.kt           // color + arrow per digit
     │   │   │   │   ├─ RoundResult.kt
     │   │   │   │   └─ GameUiState.kt
     │   │   │   └─ preferences/
     │   │   │       └─ SettingsDataStore.kt       // high scores, best streak, settings
     │   │   │
     │   │   ├─ domain/
     │   │   │   └─ GameEngine.kt                  // round loop, lives, streak, score
     │   │   │
     │   │   └─ ui/
     │   │       ├─ navigation/
     │   │       │   ├─ HexMasterNavGraph.kt
     │   │       │   └─ Screen.kt                  // sealed class of routes
     │   │       │
     │   │       ├─ theme/
     │   │       │   ├─ Color.kt                   // neon arcade palette
     │   │       │   ├─ Type.kt                    // retro/pixel display font
     │   │       │   ├─ Shape.kt
     │   │       │   └─ Theme.kt
     │   │       │
     │   │       ├─ components/
     │   │       │   ├─ ColorSwatch.kt
     │   │       │   ├─ HexOtpInputField.kt        // 6-box OTP-style hex entry, auto-advance
     │   │       │   ├─ DigitFeedbackRow.kt        // Easy mode: 6 colored digits + arrows (always shown, not color-only)
     │   │       │   ├─ ArrowIndicator.kt
     │   │       │   ├─ SimilarityMeter.kt         // Medium/Hard mode: % gauge
     │   │       │   ├─ LivesDisplay.kt
     │   │       │   ├─ StreakDisplay.kt
     │   │       │   ├─ RoundTimerBar.kt           // Hard mode countdown
     │   │       │   └─ ArcadeButton.kt
     │   │       │
     │   │       └─ screens/
     │   │           ├─ home/
     │   │           │   ├─ HomeScreen.kt
     │   │           │   └─ HomeViewModel.kt
     │   │           ├─ difficultyselect/
     │   │           │   └─ DifficultySelectScreen.kt
     │   │           ├─ game/
     │   │           │   ├─ GameScreen.kt
     │   │           │   └─ GameViewModel.kt
     │   │           ├─ gameover/
     │   │           │   └─ GameOverScreen.kt
     │   │           └─ settings/
     │   │               ├─ SettingsScreen.kt
     │   │               └─ SettingsViewModel.kt
     │   │
     │   ├─ res/
     │   │   ├─ raw/                                // sfx: user-supplied files go here
     │   │   │                                       // (correct.*, wrong.*, tick.* — see AGENTS.md)
     │   │   ├─ mipmap*/                             // app icon: user-supplied
     │   │   └─ values/ (strings, colors fallback)
     │   │
     │   └─ AndroidManifest.xml
     │
     ├─ test/java/com/abra/hexmaster/
     │   ├─ core/
     │   │   ├─ ColorGeneratorTest.kt
     │   │   ├─ HexColorUtilsTest.kt
     │   │   ├─ GuessEvaluatorTest.kt
     │   │   └─ ScoreCalculatorTest.kt
     │   └─ domain/
     │       └─ GameEngineTest.kt
     │
     └─ androidTest/java/com/abra/hexmaster/
         └─ ui/                                    // optional Compose UI tests
```

Root-level:
```
HexMaster/
 ├─ app/                     (above)
 ├─ gradle/libs.versions.toml
 ├─ build.gradle.kts
 ├─ settings.gradle.kts
 ├─ AGENTS.md                (see companion file)
 └─ README.md
```

---

## 5. Screen Flow

```
Home ──► Difficulty Select ──► Game ──(0 lives)──► Game Over ──┬──► Home
  │                                                     ▲       │
  │                                                     │       └─(Retry)─► [dialog: same difficulty ↺ / change difficulty ►Difficulty Select]
  └───────────────► Settings ◄─────────────────────────┘
```

- **Home:** title, "Play", "Settings", high-score summary — overall best plus
  a per-difficulty breakdown (Easy/Medium/Hard).
- **Difficulty Select:** Easy / Medium / Hard cards, brief rule reminder on each.
- **Game:** color swatch, `HexOtpInputField` (6-box OTP-style entry), submit
  button, lives, streak, score, and — depending on difficulty — either
  `DigitFeedbackRow` (Easy) or `SimilarityMeter` (Medium/Hard) +
  `RoundTimerBar` (Hard only).
- **Game Over:** final score, streak, "new high score" banner if either the
  overall or per-difficulty record was beaten. Retry button opens a small
  dialog: **same difficulty** (instant restart) or **change difficulty**
  (routes to Difficulty Select). Separate Home button always available.
- **Settings:** sound toggle, vibration toggle, reset high scores.

---

## 6. Visual Style — Direction

**v1 approach:** build `ui/theme/Color.kt`, `Type.kt`, `Shape.kt`, `Theme.kt`
using Material 3's default dynamic/baseline color scheme and typography —
no custom neon palette or pixel font yet. Structure these files so they're
easy to swap wholesale later (single source of truth per concern, no
hardcoded M3 values scattered into individual screens/components).

- Animations: punchy scale+color transitions on correct/incorrect feedback,
  horizontal shake on invalid input (see §3.3.1), flash/transition on life
  loss — implement with Compose's built-in animation APIs, keep them
  swappable/tunable rather than deeply baked into layout code.
- Sound: `res/raw/` expects user-supplied files (see AGENTS.md for expected
  filenames/trigger points) — wire up the playback calls but don't fabricate
  placeholder audio.
- App icon: user-supplied via `res/mipmap*/` — use the Android Studio default
  launcher icon as a placeholder until supplied, don't generate a custom one.

---

## 7. Suggested Build Order

1. Gradle scaffold + empty Compose "Hello HexMaster" screen, confirm it builds/runs.
2. `core/color` + `core/scoring` (pure Kotlin, unit-test as you go — no UI needed).
3. `domain/GameEngine` wired to those, with unit tests for the round loop
   (life loss, streak, game-over transition).
4. `data/preferences/SettingsDataStore` (high scores, settings).
5. Theme (`ui/theme`) — get the arcade look established early since it
   influences every component after.
6. Build screens bottom-up: Game screen first (it's the core loop), then
   Home → DifficultySelect → GameOver → Settings.
7. Polish pass: animations, sound, haptics, glow effects.

---

## 8. Open Tuning Knobs (intentionally left adjustable)

All centralized in `GameBalanceConfig.kt` so they're easy to rebalance without
hunting through the codebase:
- Starting lives (default 3)
- Pass threshold for Medium/Hard (default 70%)
- Hard mode timer length (default 20s)
- Base points per difficulty (100/150/200)
- Speed bonus curve

Visual theme (exact colors/typography beyond M3 defaults) is intentionally
deferred — see §6 — and isn't a launch blocker.
