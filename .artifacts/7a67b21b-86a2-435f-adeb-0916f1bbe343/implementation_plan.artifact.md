# Implementation Plan - Cancel Confirmation & Score Saving

This plan addresses the requirement to show a confirmation dialog when a user wants to quit an ongoing game and ensures that scores are correctly saved to the DataStore.

## User Review Required

> [!IMPORTANT]
> **Game Termination Behavior**: When a user confirms they want to quit, the app will save their current score and streak, then navigate to the Game Over screen. This treats the "Quit" action as an early game-over.

## Proposed Changes

### Core Logic & Scoring

#### [MODIFY] [GameViewModel.kt](file:///home/abrawahib/AndroidStudioProjects/HexMaster/app/src/main/java/com/abra/hexmaster/ui/screens/game/GameViewModel.kt)
- Inject `SettingsDataStore` into the constructor.
- Add a private `saveResult()` method that calls `settingsDataStore.saveHighScore` and `saveBestStreak`.
- Update `submitGuess()` to call `saveResult()` if `uiState.value.isGameOver` becomes true.
- Add a new event `GameEvent.OnQuitConfirmed` to handle the case where the user confirms quitting via the dialog. This event will call `saveResult()` and then trigger navigation to the Game Over screen.

### UI Screens

#### [MODIFY] [GameScreen.kt](file:///home/abrawahib/AndroidStudioProjects/HexMaster/app/src/main/java/com/abra/hexmaster/ui/screens/game/GameScreen.kt)
- Add a state variable `showQuitDialog` to manage the visibility of the confirmation alert.
- Use `BackHandler` from `androidx.activity.compose` to intercept the system back button and set `showQuitDialog = true`.
- Update the `GameTopBar` back button to also set `showQuitDialog = true` instead of calling `onBack` directly.
- Implement the `QuitConfirmationDialog` with options to "Stay" (dismiss dialog) or "Quit" (trigger `GameEvent.OnQuitConfirmed`).

### Navigation

#### [MODIFY] [HexMasterNavGraph.kt](file:///home/abrawahib/AndroidStudioProjects/HexMaster/app/src/main/java/com/abra/hexmaster/ui/navigation/HexMasterNavGraph.kt)
- Update `GameViewModel.Factory` to include `appContainer.settingsDataStore`.

## Verification Plan

### Manual Verification
1. **Natural Game Over**: Play until 0 lives. Verify that the score and streak are saved (check the Home screen high scores after returning).
2. **Quit Confirmation**:
   - Start a game and score some points.
   - Press the system back button. Verify the dialog appears.
   - Click "Stay". Verify you return to the game.
   - Press the top bar back button. Verify the dialog appears.
   - Click "Quit". Verify you are taken to the Game Over screen and your score is reflected in the high scores on the Home screen.
