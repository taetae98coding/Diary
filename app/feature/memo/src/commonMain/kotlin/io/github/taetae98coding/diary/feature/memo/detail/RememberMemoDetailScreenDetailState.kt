package io.github.taetae98coding.diary.feature.memo.detail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.Color
import io.github.taetae98coding.diary.core.design.system.diary.color.rememberDiaryColorState
import io.github.taetae98coding.diary.core.design.system.diary.component.rememberDiaryComponentState
import io.github.taetae98coding.diary.core.design.system.diary.date.rememberDiaryDateState
import io.github.taetae98coding.diary.core.model.memo.MemoDetail

@Composable
internal fun rememberMemoDetailScreenDetailState(
	onDelete: () -> Unit,
	onUpdate: () -> Unit,
	detailProvider: () -> MemoDetail?,
): MemoDetailScreenState.Detail {
	val detail = detailProvider()
	val coroutineScope = rememberCoroutineScope()
	val componentState =
		rememberDiaryComponentState(
			inputs = arrayOf(detail?.title, detail?.description),
			initialTitle = detail?.title.orEmpty(),
			initialDescription = detail?.description.orEmpty(),
		)
	val dateState =
		rememberDiaryDateState(
			inputs = arrayOf(detail?.start, detail?.endInclusive),
			initialStart = detail?.start,
			initialEndInclusive = detail?.endInclusive,
		)
	val colorState =
		rememberDiaryColorState(
			inputs = arrayOf(detail?.color),
			initialColor = detail?.color?.let { Color(it) } ?: Color.Unspecified,
		)

	return remember(componentState, dateState, colorState) {
		MemoDetailScreenState.Detail(
			onDelete = onDelete,
			onUpdate = onUpdate,
			coroutineScope = coroutineScope,
			componentState = componentState,
			dateState = dateState,
			colorState = colorState,
		)
	}
}
