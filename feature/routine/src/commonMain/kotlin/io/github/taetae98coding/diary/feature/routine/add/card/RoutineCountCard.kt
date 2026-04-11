package io.github.taetae98coding.diary.feature.routine.add.card

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.taetae98coding.diary.compose.core.icon.AddIcon
import io.github.taetae98coding.diary.compose.core.icon.RemoveIcon
import io.github.taetae98coding.diary.compose.core.theme.DiaryTheme

@Composable
internal fun RoutineCountCard(
    state: RoutineCountCardState,
    modifier: Modifier = Modifier,
) {
    Card(modifier = modifier) {
        Text(
            text = "목표 횟수",
            style = DiaryTheme.typography.titleMedium,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(
                onClick = state::decrement,
                enabled = state.count > 1,
            ) {
                RemoveIcon()
            }
            Text(
                text = "${state.count}",
                style = DiaryTheme.typography.headlineMedium,
                modifier = Modifier.padding(horizontal = 24.dp),
            )
            IconButton(onClick = state::increment) {
                AddIcon()
            }
        }
    }
}
