@file:JvmName("WeekItemRowKt")

package io.github.taetae98coding.diary.core.calendar.compose.week

import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import io.github.taetae98coding.diary.core.calendar.compose.CalendarDefaults
import io.github.taetae98coding.diary.core.calendar.compose.color.CalendarColors
import io.github.taetae98coding.diary.core.design.system.theme.DiaryTheme
import io.github.taetae98coding.diary.library.color.toContrastColor
import kotlin.jvm.JvmName

@Composable
internal fun WeekItemRow(
    items: List<WeekItem>,
    onWeekItemClick: (Any) -> Unit,
    modifier: Modifier = Modifier,
    colors: CalendarColors = CalendarDefaults.colors(),
) {
    Row(modifier = modifier.height(IntrinsicSize.Min)) {
        items.forEach {
            when (it) {
                is WeekItem.Space -> {
                    Spacer(modifier = Modifier.weight(it.weight))
                }

                is WeekItem.Holiday -> {
                    key(it.key) {
                        WeekTextItem(
                            text = it.name,
                            color = colors.sundayColor,
                            modifier = Modifier.weight(it.weight)
                                .fillMaxHeight()
                                .padding(horizontal = 1.dp)
                                .clip(RoundedCornerShape(4.dp)),
                        )
                    }
                }

                is WeekItem.Text -> {
                    key(it.key) {
                        WeekTextItem(
                            text = it.name,
                            color = it.color,
                            modifier = Modifier.weight(it.weight)
                                .fillMaxHeight()
                                .padding(horizontal = 1.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .clickable { onWeekItemClick(it.key) },
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun WeekTextItem(
    text: String,
    color: Color,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier.background(color = color),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = text,
            modifier = Modifier.basicMarquee(iterations = Int.MAX_VALUE)
                .padding(2.dp),
            color = color.toContrastColor(),
            textAlign = TextAlign.Center,
            maxLines = 1,
            style = DiaryTheme.typography.labelMedium,
        )
    }
}
