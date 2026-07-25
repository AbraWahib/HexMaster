package com.abra.hexmaster.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.key.*
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun HexOtpInputField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    isError: Boolean = false,
    onShakeComplete: () -> Unit = {}
) {
    val focusRequesters = remember { List(6) { FocusRequester() } }
    
    // Auto-focus first box when input is empty
    LaunchedEffect(value) {
        if (value.isBlank()) {
            focusRequesters[0].requestFocus()
        }
    }

    // Shake animation
    val shakeOffset = remember { Animatable(0f) }
    LaunchedEffect(isError) {
        if (isError) {
            repeat(4) {
                shakeOffset.animateTo(10f, animationSpec = tween(50, easing = LinearEasing))
                shakeOffset.animateTo(-10f, animationSpec = tween(50, easing = LinearEasing))
            }
            shakeOffset.animateTo(0f, animationSpec = tween(50, easing = LinearEasing))
            onShakeComplete()
        }
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .graphicsLayer(translationX = shakeOffset.value),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        (0 until 6).forEach { index ->
            val digit = value.getOrNull(index)?.toString() ?: ""
            
            HexDigitBox(
                digit = digit,
                isError = isError,
                onDigitChange = { char ->
                    if (char.length <= 1 && (char.isEmpty() || char.first().isDigit() || char.first().lowercaseChar() in 'a'..'f')) {
                        val newList = value.padEnd(6, ' ').toCharArray()
                        
                        if (char.isEmpty()) {
                            newList[index] = ' '
                        } else {
                            newList[index] = char.first().uppercaseChar()
                        }
                        
                        val newValue = String(newList)
                        onValueChange(newValue)

                        if (char.isNotEmpty() && index < 5) {
                            focusRequesters[index + 1].requestFocus()
                        }
                    }
                },
                onBackspace = {
                    if (index > 0) {
                        val newList = value.padEnd(6, ' ').toCharArray()
                        newList[index] = ' '
                        onValueChange(String(newList))
                        focusRequesters[index - 1].requestFocus()
                    }
                },
                focusRequester = focusRequesters[index]
            )
        }
    }
}

@Composable
private fun HexDigitBox(
    digit: String,
    isError: Boolean,
    onDigitChange: (String) -> Unit,
    onBackspace: () -> Unit,
    focusRequester: FocusRequester
) {
    Surface(
        modifier = Modifier
            .size(48.dp, 64.dp)
            .border(
                width = 2.dp,
                color = if (isError) Color.Red else MaterialTheme.colorScheme.outline,
                shape = MaterialTheme.shapes.small
            ),
        shape = MaterialTheme.shapes.small,
        color = MaterialTheme.colorScheme.surface
    ) {
        Box(contentAlignment = Alignment.Center) {
            BasicTextField(
                value = if (digit == " ") "" else digit,
                onValueChange = onDigitChange,
                modifier = Modifier
                    .focusRequester(focusRequester)
                    .onPreviewKeyEvent { keyEvent ->
                        if (keyEvent.type == KeyEventType.KeyDown && keyEvent.key == Key.Backspace) {
                            onBackspace()
                            true
                        } else {
                            false
                        }
                    },
                textStyle = TextStyle(
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurface
                ),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Ascii),
                singleLine = true,
                decorationBox = { innerTextField ->
                    if (digit.isBlank()) {
                        Text(
                            text = "_",
                            style = TextStyle(
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Light,
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colorScheme.outlineVariant
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    innerTextField()
                }
            )
        }
    }
}
