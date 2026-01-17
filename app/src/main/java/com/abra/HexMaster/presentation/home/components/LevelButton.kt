package com.abra.HexMaster.presentation.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.abra.HexMaster.R
import com.abra.HexMaster.ui.theme.EasyLevelBackGround
import com.abra.HexMaster.ui.theme.EasyLevelTint
import com.abra.HexMaster.ui.theme.HardLevelBackGround
import com.abra.HexMaster.ui.theme.HardLevelTint
import com.abra.HexMaster.ui.theme.MediumLevelBackGround
import com.abra.HexMaster.ui.theme.MediumLevelTint

@Composable
fun LevelButton(level: Level, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.background(MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .padding(16.dp)
        ) {
            Icon(
                painter = painterResource(level.iconId),
                contentDescription = stringResource(level.nameId),
                modifier = Modifier
                    .size(48.dp)
                    .background(level.backgroundColor, shape = RoundedCornerShape(12.dp))
                    .padding(8.dp),
                tint = level.tint
            )
            Spacer(Modifier.width(16.dp))
            Column(
                verticalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f)
            ) {
                Text(
                    stringResource(level.nameId),
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    text = stringResource(level.descriptionId),
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.onSurface.copy(
                            alpha = .6f
                        )
                    ),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Icon(painterResource(R.drawable.arrow_right), contentDescription = null, tint = level.tint)
        }
    }
}

sealed class Level(
    val nameId: Int,
    val iconId: Int,
    val tint: Color,
    val backgroundColor: Color,
    val descriptionId: Int
) {
    object Easy : Level(
        nameId = R.string.easy,
        iconId = R.drawable.easy,
        tint = EasyLevelTint,
        backgroundColor = EasyLevelBackGround,
        descriptionId = R.string.easy_description
    )

    object Medium : Level(
        nameId = R.string.medium,
        iconId = R.drawable.medium,
        tint = MediumLevelTint,
        backgroundColor = MediumLevelBackGround,
        descriptionId = R.string.medium_description
    )

    object Hard : Level(
        nameId = R.string.hard,
        iconId = R.drawable.hard,
        tint = HardLevelTint,
        backgroundColor = HardLevelBackGround,
        descriptionId = R.string.hard_description
    )
}
