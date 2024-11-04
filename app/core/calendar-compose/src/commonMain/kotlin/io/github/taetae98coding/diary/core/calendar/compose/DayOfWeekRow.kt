package io.github.taetae98coding.diary.core.calendar.compose

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.takeOrElse
import androidx.compose.ui.text.style.TextAlign
import io.github.taetae98coding.diary.core.calendar.compose.color.CalendarColors
import io.github.taetae98coding.diary.core.design.system.theme.DiaryTheme
import io.github.taetae98coding.diary.core.resources.Res
import io.github.taetae98coding.diary.core.resources.fri
import io.github.taetae98coding.diary.core.resources.mon
import io.github.taetae98coding.diary.core.resources.sat
import io.github.taetae98coding.diary.core.resources.sun
import io.github.taetae98coding.diary.core.resources.thu
import io.github.taetae98coding.diary.core.resources.tue
import io.github.taetae98coding.diary.core.resources.wed
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun DayOfWeekRow(
    modifier: Modifier = Modifier,
    colors: CalendarColors = CalendarDefaults.colors(),
) {
    Row(modifier = modifier) {
        val dayOfWeekModifier = Modifier.weight(1F)

        Text(
            text = stringResource(Res.string.sun),
            modifier = dayOfWeekModifier,
            color = colors.sundayColor,
            textAlign = TextAlign.Center,
            style = DiaryTheme.typography.bodySmall,
        )
        Text(
            text = stringResource(Res.string.mon),
            modifier = dayOfWeekModifier,
            color = colors.dayColor.takeOrElse { LocalContentColor.current },
            textAlign = TextAlign.Center,
            style = DiaryTheme.typography.bodySmall,
        )
        Text(
            text = stringResource(Res.string.tue),
            modifier = dayOfWeekModifier,
            color = colors.dayColor.takeOrElse { LocalContentColor.current },
            textAlign = TextAlign.Center,
            style = DiaryTheme.typography.bodySmall,
        )
        Text(
            text = stringResource(Res.string.wed),
            modifier = dayOfWeekModifier,
            color = colors.dayColor.takeOrElse { LocalContentColor.current },
            textAlign = TextAlign.Center,
            style = DiaryTheme.typography.bodySmall,
        )
        Text(
            text = stringResource(Res.string.thu),
            modifier = dayOfWeekModifier,
            color = colors.dayColor.takeOrElse { LocalContentColor.current },
            textAlign = TextAlign.Center,
            style = DiaryTheme.typography.bodySmall,
        )
        Text(
            text = stringResource(Res.string.fri),
            modifier = dayOfWeekModifier,
            color = colors.dayColor.takeOrElse { LocalContentColor.current },
            textAlign = TextAlign.Center,
            style = DiaryTheme.typography.bodySmall,
        )
        Text(
            text = stringResource(Res.string.sat),
            modifier = dayOfWeekModifier,
            color = colors.saturdayColor,
            textAlign = TextAlign.Center,
            style = DiaryTheme.typography.bodySmall,
        )
    }
}
