# Implementation Plan - Scrollable Guess History & Logic Fix

The user wants to see all previous guesses in a scrollable column and fix the "Easy" mode feedback logic to match specific thresholds.

## Proposed Changes

### Logic Fix (Easy Mode)

#### [MODIFY] [GuessEvaluator.kt](file:///home/abrawahib/AndroidStudioProjects/HexMaster/app/src/main/java/com/abra/hexmaster/core/scoring/GuessEvaluator.kt)
- Update arrow logic for Easy mode:
    - `diff == 0` -> `NONE`
    - `diff in 1..2` -> `UP`/`DOWN` (matches YELLOW)
    - `diff > 2` -> `UP_UP`/`DOWN_DOWN` (matches RED)

### Data Model

#### [MODIFY] [GameUiState.kt](file:///home/abrawahib/AndroidStudioProjects/HexMaster/app/src/main/java/com/abra/hexmaster/data/model/GameUiState.kt)
- Add `previousGuesses: List<RoundResult> = emptyList()` to track all attempts in the current round.

### Domain Logic

#### [MODIFY] [GameEngine.kt](file:///home/abrawahib/AndroidStudioProjects/HexMaster/app/src/main/java/com/abra/hexmaster/domain/GameEngine.kt)
- Update `submitGuess` to append the new `RoundResult` to the `previousGuesses` list.
- Update `nextRound` and `reset` to clear the `previousGuesses` list.

### UI Layer

#### [MODIFY] [GameScreen.kt](file:///home/abrawahib/AndroidStudioProjects/HexMaster/app/src/main/java/com/abra/hexmaster/ui/screens/game/GameScreen.kt)
- Replace the single feedback view with a `LazyColumn` that displays `uiState.previousGuesses`.
- Display each guess as a card or row with its specific feedback (either `DigitFeedbackRow` or `SimilarityMeter`).
- Ensure the most recent guess is visible or at the bottom.
- Keep the `ColorSwatch` and `HexOtpInputField` visible as the primary interaction elements.

## Verification Plan

### Automated Tests
- Update `GuessEvaluatorTest.kt` to reflect the new arrow thresholds.
- Update `GameEngineTest.kt` to verify `previousGuesses` accumulation.

### Manual Verification
- Play a round in Easy mode and verify color/arrow combinations:
    - Exact match -> Green, no arrow.
    - Off by 1-2 -> Yellow, single arrow.
    - Off by 3+ -> Red, double arrow.
- Verify that multiple guesses appear in a scrollable list.
- Verify that the list clears between rounds.
