package com.abra.HexMaster.presentation.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.abra.HexMaster.presentation.easy_level.components.DigitCheckingSection

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BasicScreen(screenTitle:String, content: @Composable ()-> Unit) {
    var currentGuess by remember { mutableStateOf("") }
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                ),
                title = { Text(screenTitle, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = {}) {  // todo: implement back button functionality
                        Icon(Icons.AutoMirrored.Default.ArrowBack, "Back")
                    }
                },
            )
        },
        bottomBar = {
            Button(
                onClick = { }, // todo: implement submit button functionality
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                enabled = currentGuess.length == 6
            ) {
                Text("Submit Guess", fontWeight = FontWeight.Bold)
                Icon(Icons.AutoMirrored.Default.ArrowForward, null, Modifier.padding(start = 8.dp))
            }
        }
    ) { paddingValues ->
        MaterialTheme.shapes
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(18.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(280.dp)
                    .clip(MaterialTheme.shapes.large)
                    .background(Color(0xFFB39DDB)) // todo: replace with actual color
            )
            Spacer(Modifier.height(24.dp))
            val textFieldState = rememberTextFieldState()
            GuessingSection(state = textFieldState)
            Spacer(Modifier.height(24.dp))
            content()
        }
    }
}
