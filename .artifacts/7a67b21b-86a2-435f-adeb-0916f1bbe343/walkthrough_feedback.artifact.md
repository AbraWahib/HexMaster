# Detailed Feedback for Medium & Hard Modes

I have enhanced the feedback system for the Medium and Hard difficulty levels. Instead of showing only the similarity percentage, the app now provides a visual and textual comparison between the target color and your guess.

## Changes Made

### 🎨 Visual Comparison
- **`SimilarityMeter.kt`**: This component has been redesigned. It now features a three-column layout:
    - **TARGET**: Shows a circular swatch of the color you are trying to match, along with its hex code.
    - **SIMILARITY**: Displays the calculated accuracy percentage and a colored progress bar.
    - **GUESS**: Shows a circular swatch of the color you entered, along with your guessed hex code.
- **Improved Feedback**: Each attempt in your guess history now acts as a miniature comparison tool, helping you see exactly how your color differed from the target.

### 🎮 Game Screen Integration
- **`GameScreen.kt`**: Updated to feed the specific guess and target color data into the history list. As you make multiple tries, each entry in the scrollable list will show this detailed breakdown.

## Verification

### Manual Check (Recommended)
1. Start a game in **Medium** or **Hard** mode.
2. Enter a hex code like `FF0000` (Red) when the target is something else.
3. Observe the history card:
    - You should see two circles side-by-side.
    - The left circle is the color you should have guessed.
    - The right circle is the red you actually guessed.
    - The center shows the percentage (e.g., "45% Similarity").

> [!TIP]
> This change only affects Medium and Hard modes, as Easy mode already uses a per-digit feedback system that provides granular hints.
