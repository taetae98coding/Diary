package io.github.taetae98coding.diary.feature.routine.add.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import io.github.taetae98coding.diary.compose.core.preview.ComponentPreview
import io.github.taetae98coding.diary.compose.core.theme.DiaryTheme

@Composable
internal fun CalendarVisibilityCard(
    state: CalendarVisibilityCardState,
    modifier: Modifier = Modifier,
) {
    Card(modifier = modifier) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Column(
                modifier = Modifier.weight(1F),
                verticalArrangement = Arrangement.spacedBy(2.dp),
            ) {
                Text(
                    text = "캘린더에 표시",
                    style = DiaryTheme.typography.titleSmall,
                )
                Text(
                    text = "끄면 이 루틴은 캘린더에 표시되지 않습니다.",
                    style = DiaryTheme.typography.bodySmall,
                )
            }
            Switch(
                checked = state.isVisible,
                onCheckedChange = state::update,
            )
        }
    }
}

@ComponentPreview
@Composable
private fun Preview() {
    DiaryTheme {
        CalendarVisibilityCard(state = CalendarVisibilityCardState(initialVisible = true))
    }
}
