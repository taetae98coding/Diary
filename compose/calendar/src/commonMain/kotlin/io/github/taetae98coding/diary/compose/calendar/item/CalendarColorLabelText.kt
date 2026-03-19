package io.github.taetae98coding.diary.compose.calendar.item

import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import io.github.taetae98coding.diary.compose.core.theme.DiaryTheme

public const val CALENDAR_COLOR_LABEL_TEXT_CONTENT_TYPE: String = "CALENDAR_COLOR_LABEL_TEXT_CONTENT_TYPE"

@Composable
public fun CalendarColorLabelText(
    text: String,
    color: Color,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.height(IntrinsicSize.Min),
        horizontalArrangement = Arrangement.spacedBy(2.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier.width(4.dp)
                .fillMaxHeight()
                .background(color = color, shape = RoundedCornerShape(4.dp)),
        )
        Text(
            text = text,
            modifier = Modifier.weight(1F)
                .padding(2.dp)
                .basicMarquee(iterations = Int.MAX_VALUE),
            textAlign = TextAlign.Center,
            maxLines = 1,
            style = DiaryTheme.typography.bodyMedium,
        )
    }
}
