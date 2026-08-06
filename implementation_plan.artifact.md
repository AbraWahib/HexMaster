# HexMaster Modifications Plan

This plan outlines the implementation of 6 tries per round, improved backspace navigation in the OTP input, and a "How to Play" dialog with a soft transparent look.

## Proposed Changes

### [Component Name]

#### [MODIFY] [GameUiState.kt](file:///home/abrawahib/AndroidStudioProjects/HexMaster/app/src/main/java/com/abra/hexmaster/data/model/GameUiState.kt)
- Add `triesUsed: Int = 0` to track attempts in the current round.
- Add `maxTries: Int = 6` as a constant (or pull from `GameBalanceConfig`).

#### [MODIFY] [GameEngine.kt](file:///home/abrawahib/AndroidStudioProjects/HexMaster/app/src/main/java/com/abra/hexmaster/domain/GameEngine.kt)
- Update `submitGuess` logic:
    - Increment `triesUsed` on every valid submission.
    - If guess is correct:
        - Set `isPass = true`.
        - Update score and streak.
        - Round concludes (user must press "Next").
    - If guess is incorrect:
        - If `triesUsed >= 6`:
            - Set `isPass = false`.
            - Decrement `lives`.
            - Reset `streak`.
            - Round concludes (user must press "Next").
        - Else:
            - Round continues. User can try again.
- Update `nextRound` to reset `triesUsed` to 0.
- Update `reset` to reset `triesUsed` to 0.

#### [MODIFY] [GameBalanceConfig.kt](file:///home/abrawahib/AndroidStudioProjects/HexMaster/app/src/main/java/com/abra/hexmaster/core/GameBalanceConfig.kt)
- Add `MAX_TRIES_PER_ROUND = 6`.

#### [MODIFY] [HexOtpInputField.kt](file:///home/abrawahib/AndroidStudioProjects/HexMaster/app/src/main/java/com/abra/hexmaster/ui/components/HexOtpInputField.kt)
- Improve backspace logic: ensure that pressing backspace in an empty or non-empty box (depending on user preference, but usually if empty) moves focus to the previous box.
- Refine `onKeyEvent` to handle focus shifting more smoothly.

#### [MODIFY] [HomeScreen.kt](file:///home/abrawahib/AndroidStudioProjects/HexMaster/app/src/main/java/com/abra/hexmaster/ui/screens/home/HomeScreen.kt)
- Add an Info/Help icon to the `TopAppBar`.
- Implement a `HowToPlayDialog` with a semi-transparent background and clear instructions.

#### [MODIFY] [GameScreen.kt](file:///home/abrawahib/AndroidStudioProjects/HexMaster/app/src/main/java/com/abra/hexmaster/ui/screens/game/GameScreen.kt)
- Display the number of tries used (e.g., "Try 1/6").
- Ensure the UI correctly transitions between attempts within the same round.

## Verification Plan

### Automated Tests
- Update `GameEngineTest.kt` to verify the 6-tries logic (life only lost after 6th failure).
- Verify backspace navigation manually in the emulator.

### Manual Verification
- Play a round and intentionally fail 5 times, then succeed on the 6th to verify score/streak.
- Fail 6 times to verify life loss.
- Check the "How to Play" dialog for transparency and layout.
- Test backspace behavior in the OTP input.
