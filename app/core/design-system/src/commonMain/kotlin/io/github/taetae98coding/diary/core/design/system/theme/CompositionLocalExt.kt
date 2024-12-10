package io.github.taetae98coding.diary.core.design.system.theme

import androidx.compose.runtime.staticCompositionLocalOf
import io.github.taetae98coding.diary.core.design.system.color.DiaryColor
import io.github.taetae98coding.diary.core.design.system.dimen.DiaryDimen
import io.github.taetae98coding.diary.core.design.system.shape.DiaryShape
import io.github.taetae98coding.diary.core.design.system.typography.DiaryTypography

private const val MESSAGE = "DiaryTheme not found."

internal val LocalDiaryColor = staticCompositionLocalOf<DiaryColor> { error(MESSAGE) }
internal val LocalDiaryShape = staticCompositionLocalOf<DiaryShape> { error(MESSAGE) }
internal val LocalDiaryTypography = staticCompositionLocalOf<DiaryTypography> { error(MESSAGE) }
internal val LocalDiaryDimen = staticCompositionLocalOf<DiaryDimen> { error(MESSAGE) }
