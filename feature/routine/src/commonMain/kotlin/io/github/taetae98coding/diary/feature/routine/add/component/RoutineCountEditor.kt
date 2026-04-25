package io.github.taetae98coding.diary.feature.routine.add.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.taetae98coding.diary.compose.core.icon.AddIcon
import io.github.taetae98coding.diary.compose.core.icon.RemoveIcon
import io.github.taetae98coding.diary.compose.core.preview.ComponentPreview
import io.github.taetae98coding.diary.compose.core.theme.DiaryTheme

@Composable
internal fun RoutineCountEditor(
    state: RoutineCountEditorState,
    modifier: Modifier = Modifier,
) {
    Card(modifier = modifier) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(
                text = "수행 횟수",
                style = DiaryTheme.typography.titleSmall,
                modifier = Modifier.weight(1F),
            )
            FilledTonalIconButton(
                onClick = state::decrement,
                enabled = state.count > 1,
            ) {
                RemoveIcon()
            }
            Text(
                text = "${state.count}회",
                style = DiaryTheme.typography.titleMedium,
            )
            FilledTonalIconButton(onClick = state::increment) {
                AddIcon()
            }
        }
    }
}

@ComponentPreview
@Composable
private fun Preview() {
    DiaryTheme {
        RoutineCountEditor(state = RoutineCountEditorState(initialCount = 3))
    }
}
