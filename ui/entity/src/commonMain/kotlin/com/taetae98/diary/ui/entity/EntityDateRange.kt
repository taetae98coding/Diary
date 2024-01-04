package com.taetae98.diary.ui.entity

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
public fun EntityDateRange(
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.width(intrinsicSize = IntrinsicSize.Min)
    ) {
        Column {
            SwitchLayout(modifier = Modifier.fillMaxWidth())
            ColorLayout(modifier = Modifier.fillMaxWidth())
        }
    }
}

@Composable
private fun SwitchLayout(
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            modifier = Modifier.weight(1F),
            text = "캘린더"
        )
        Switch(
            checked = true,
            onCheckedChange = null,
        )
    }
}

@Composable
private fun ColorLayout(
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            modifier = Modifier.weight(1F),
            text = "컬러",
        )

        Box(
            modifier = Modifier.size(32.dp)
                .clip(CircleShape)
                .border(
                    width = 2.dp,
                    color = MaterialTheme.colorScheme.outlineVariant,
                    shape = CircleShape,
                )
                .drawBehind {
                    drawCircle(Color.Red)
                }
        )
    }
}