# Hex Color Guessing Game 🎨

A color-guessing game that challenges players to identify the hexadecimal code of a displayed color, with selectable difficulty levels.

## 🎚️ Difficulty Selection

Before starting the game, the player can choose a difficulty level.

Difficulty affects:
- Allowed error margin
- Precision required to succeed
- Feedback strictness

Example:
- Easy: Large tolerance, forgiving feedback
- Medium: Moderate accuracy required
- Hard: Very strict color matching

## 🎯 How the Game Works

1. The player selects a difficulty level.
2. A random color is displayed.
3. The player enters a hex color guess (e.g. `#FF5733`).
4. The game compares the guess with the correct color.
5. Feedback is shown based on the selected difficulty.

## 🧠 Feedback System

The game compares RGB values of the guessed color and the target color.  
The calculated distance determines how close the guess is and whether it passes the selected difficulty requirements.

## ✨ Features

- User-selectable difficulty levels
- Real-time accuracy feedback
- Hex color validation
- Simple and clean UI
- Educational gameplay

## 🛠️ Technologies Used

- Language: _( Kotlin )_
- UI: _( Jetpack Compose )_
- RGB distance-based color comparison
