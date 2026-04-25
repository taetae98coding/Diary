package io.github.taetae98coding.diary.feature.routine.home

import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import io.github.taetae98coding.diary.compose.core.color.ColorCircle
import io.github.taetae98coding.diary.compose.core.theme.DiaryTheme
import io.github.taetae98coding.diary.core.model.routine.Routine

@Composable
internal fun RoutineCard(
    modifier: Modifier = Modifier,
    uiState: Routine? = null,
    onClick: () -> Unit = {},
) {
    Card(
        onClick = onClick,
        modifier = modifier,
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            ColorCircle(
                colorProvider = { uiState?.detail?.color?.let(::Color) ?: Color.Unspecified },
                modifier = Modifier.size(8.dp),
            )
            Text(
                text = uiState?.detail?.title.orEmpty(),
                modifier = Modifier
                    .weight(1f)
                    .basicMarquee(iterations = Int.MAX_VALUE),
                maxLines = 1,
                style = DiaryTheme.typography.titleMedium,
            )
        }
    }
}
