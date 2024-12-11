package io.github.taetae98coding.diary.core.compose.memo.add

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import io.github.taetae98coding.diary.core.compose.memo.detail.MemoDetailScaffoldState
import io.github.taetae98coding.diary.core.design.system.diary.color.rememberDiaryColorState
import io.github.taetae98coding.diary.core.design.system.diary.component.rememberDiaryComponentState
import io.github.taetae98coding.diary.core.design.system.diary.date.rememberDiaryDateState
import kotlinx.datetime.LocalDate

@Composable
public fun rememberMemoDetailScaffoldAddState(
	initialStart: LocalDate?,
	initialEndInclusive: LocalDate?,
): MemoDetailScaffoldState.Add {
	val coroutineScope = rememberCoroutineScope()
	val componentState = rememberDiaryComponentState()
	val dateState =
		rememberDiaryDateState(
			initialStart = initialStart,
			initialEndInclusive = initialEndInclusive,
		)
	val colorState = rememberDiaryColorState()

	return remember {
		MemoDetailScaffoldState.Add(
			coroutineScope = coroutineScope,
			componentState = componentState,
			dateState = dateState,
			colorState = colorState,
		)
	}
}
