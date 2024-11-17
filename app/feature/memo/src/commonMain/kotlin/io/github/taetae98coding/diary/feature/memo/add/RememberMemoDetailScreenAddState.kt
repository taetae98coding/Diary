package io.github.taetae98coding.diary.feature.memo.add

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import io.github.taetae98coding.diary.core.design.system.diary.color.rememberDiaryColorState
import io.github.taetae98coding.diary.core.design.system.diary.component.rememberDiaryComponentState
import io.github.taetae98coding.diary.core.design.system.diary.date.rememberDiaryDateState
import io.github.taetae98coding.diary.feature.memo.detail.MemoDetailScreenState
import kotlinx.datetime.LocalDate

@Composable
internal fun rememberMemoDetailScreenAddState(
    initialStart: LocalDate?,
    initialEndInclusive: LocalDate?,
): MemoDetailScreenState.Add {
    val coroutineScope = rememberCoroutineScope()
    val componentState = rememberDiaryComponentState()
    val dateState = rememberDiaryDateState(
        initialStart = initialStart,
        initialEndInclusive = initialEndInclusive,
    )
    val colorState = rememberDiaryColorState()

    return remember {
        MemoDetailScreenState.Add(
            coroutineScope = coroutineScope,
            componentState = componentState,
            dateState = dateState,
            colorState = colorState,
        )
    }
}
