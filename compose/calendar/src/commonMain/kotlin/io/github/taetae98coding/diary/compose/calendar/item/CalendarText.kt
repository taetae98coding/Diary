package io.github.taetae98coding.diary.compose.calendar.item

import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import io.github.taetae98coding.diary.compose.core.theme.DiaryTheme
import io.github.taetae98coding.diary.library.compose.ui.wcagAAAContentColor

public const val CALENDAR_TEXT_CONTENT_TYPE: String = "CALENDAR_TEXT_CONTENT_TYPE"

@Composable
public fun CalendarText(
    text: String,
    color: Color,
    modifier: Modifier = Modifier,
) {
    Text(
        text = text,
        modifier = modifier.background(color, RoundedCornerShape(4.dp))
            .padding(horizontal = 2.dp, vertical = 4.dp)
            .basicMarquee(iterations = Int.MAX_VALUE),
        color = color.wcagAAAContentColor(),
        textAlign = TextAlign.Center,
        maxLines = 1,
        style = DiaryTheme.typography.bodySmall,
    )
}
