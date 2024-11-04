package io.github.taetae98coding.diary.core.design.system.theme

import androidx.compose.runtime.staticCompositionLocalOf
import io.github.taetae98coding.diary.core.design.system.color.DiaryColor
import io.github.taetae98coding.diary.core.design.system.dimen.DiaryDimen
import io.github.taetae98coding.diary.core.design.system.typography.DiaryTypography

private const val Message = "DiaryTheme not found."

internal val LocalDiaryColor = staticCompositionLocalOf<DiaryColor> { error(Message) }
internal val LocalDiaryTypography = staticCompositionLocalOf<DiaryTypography> { error(Message) }
internal val LocalDiaryDimen = staticCompositionLocalOf<DiaryDimen> { error(Message) }
