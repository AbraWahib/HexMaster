package com.abra.hexmaster.ui.screens.difficultyselect

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.abra.hexmaster.R
import com.abra.hexmaster.data.model.Difficulty

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DifficultySelectScreen(
    onDifficultySelected: (Difficulty) -> Unit,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("SELECT DIFFICULTY") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            DifficultyCard(
                difficulty = Difficulty.EASY,
                title = stringResource(R.string.easy),
                description = stringResource(R.string.easy_description),
                onClick = { onDifficultySelected(Difficulty.EASY) }
            )
            DifficultyCard(
                difficulty = Difficulty.MEDIUM,
                title = stringResource(R.string.medium),
                description = stringResource(R.string.medium_description),
                onClick = { onDifficultySelected(Difficulty.MEDIUM) }
            )
            DifficultyCard(
                difficulty = Difficulty.HARD,
                title = stringResource(R.string.hard),
                description = stringResource(R.string.hard_description),
                onClick = { onDifficultySelected(Difficulty.HARD) }
            )
        }
    }
}

@Composable
fun DifficultyCard(
    difficulty: Difficulty,
    title: String,
    description: String,
    onClick: () -> Unit
) {
    val color = when (difficulty) {
        Difficulty.EASY -> MaterialTheme.colorScheme.primary
        Difficulty.MEDIUM -> MaterialTheme.colorScheme.secondary
        Difficulty.HARD -> MaterialTheme.colorScheme.error
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = title.uppercase(),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Black,
                color = color
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
