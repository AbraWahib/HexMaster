package com.abra.HexMaster.presentation.main

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.abra.HexMaster.ui.theme.HexMasterTheme

@Composable
fun GuessingSection(modifier: Modifier = Modifier, state: TextFieldState) {
    val textFieldStyle = MaterialTheme.typography.headlineSmall.copy(
        letterSpacing = 6.sp,
        color = MaterialTheme.colorScheme.secondary
    )
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = "Enter your guess".uppercase(),
            style = MaterialTheme.typography.titleSmall.copy(
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.secondary
            ),
            modifier = Modifier.align(Alignment.Start)
        )
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            state = state,
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                focusedBorderColor = MaterialTheme.colorScheme.secondary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline,
            ),
            modifier = Modifier.fillMaxWidth(),
            textStyle = textFieldStyle,
            placeholder = { Text("000000", style = textFieldStyle) },
            prefix = { Text("#", style = textFieldStyle, modifier = Modifier.padding(end = 8.dp)) },
            shape = MaterialTheme.shapes.large
        )
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
private fun GuessingSectionPrev() {
    HexMasterTheme {
        Surface {
            GuessingSection(state = TextFieldState())
        }
    }
}