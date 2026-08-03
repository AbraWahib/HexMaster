<p align="center">
  <img src="docs/images/app_icon.png" alt="HexMaster app icon" width="774"/>
</p>

<h1 align="center">HexMaster</h1>

<p align="center">
  An arcade-style color-guessing game for Android — see a color, guess its hex code.
</p>

---

## Overview

A random color is shown on screen. You type your best guess at its 6-digit
hex code. How much feedback you get — and how much pressure you're under —
depends on the difficulty:

| Difficulty | Feedback                                                             | Timer                     |
|------------|----------------------------------------------------------------------|---------------------------|
| **Easy**   | Per-digit color + directional arrows showing how close each digit is | None                      |
| **Medium** | Overall similarity percentage                                        | None                      |
| **Hard**   | Same as Medium                                                       | Fixed per-round countdown |

The game is endless with 3 lives — miss too many rounds and it's game over.
Score is based on accuracy (and speed, in Hard mode), with both an overall
high score and a per-difficulty breakdown saved locally.

## App Icon

<p align="center">
  <img src="docs/images/app_icon.png" alt="HexMaster app icon" width="774"/>
</p>


## Screenshots

| Home             | Game (Easy)      | Game (medium)    | Game (Hard)      | Game Over        |
|------------------|------------------|------------------|------------------|------------------|
| _add screenshot_ | _add screenshot_ | _add screenshot_ | _add screenshot_ | _add screenshot_ |

## Features

- Three difficulty modes with distinct feedback systems
- Per-digit hex grading with color + arrow indicators (colorblind-safe —
  never relies on color alone)
- Timed Hard mode with a speed-based scoring bonus
- Endless play with a 3-life system and streak tracking
- Local high scores — overall best plus a per-difficulty breakdown
- Sound & vibration toggles

## Tech Stack

- **Kotlin** + **Jetpack Compose** (Material 3)
- **MVVM** architecture (`ViewModel` + `StateFlow`)
- **Compose Navigation**
- **Jetpack DataStore** (Preferences) for high scores & settings
- Manual DI via a lightweight `AppContainer`
- **minSdk 24** (Android 7.0+)

Full architecture, file structure, and game-logic formulas are documented in
[`HexMaster_Architecture_Plan.md`](HexMaster_Architecture_Plan.md).

## Project Structure

```
app/src/main/java/com/abra/hexmaster/
 ├─ core/        → pure game logic: color generation, hex utils, scoring, grading
 ├─ data/        → models, DataStore-backed settings/high scores
 ├─ domain/      → GameEngine — round loop, lives, streak, score
 └─ ui/          → screens, components, theme, navigation
```

See [`HexMaster_Architecture_Plan.md`](HexMaster_Architecture_Plan.md) for the
full tree and rationale.

## Getting Started

1. Clone the repo and open it in Android Studio (Iguana or newer recommended).
2. Let Gradle sync — dependency versions are pinned in
   `gradle/libs.versions.toml`.
3. Run on an emulator or device with **API 24+**.

```bash
./gradlew assembleDebug     # build a debug APK
./gradlew test              # run unit tests (core game-logic + ViewModels)
```

## Contributing / Working on this project

If you're an AI coding agent (or a human) picking this project up,
**read [`AGENTS.md`](AGENTS.md) first** — it documents the binding
architectural rules, input-handling behavior, accessibility requirements, and
persistence keys that any change should respect.

## License

_Add a license of your choice (e.g. MIT) here._
