package io.github.taetae98coding.diary.core.compose.tag.detail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.Color
import io.github.taetae98coding.diary.core.design.system.diary.color.rememberDiaryColorState
import io.github.taetae98coding.diary.core.design.system.diary.component.rememberDiaryComponentState
import io.github.taetae98coding.diary.core.model.tag.TagDetail

@Composable
public fun rememberTagDetailScaffoldDetailState(
	onUpdate: () -> Unit,
	onDelete: () -> Unit,
	navigateToMemo: () -> Unit,
	detailProvider: () -> TagDetail?,
): TagDetailScaffoldState.Detail {
	val detail = detailProvider()
	val coroutineScope = rememberCoroutineScope()
	val componentState =
		rememberDiaryComponentState(
			inputs = arrayOf(detail?.title, detail?.description),
			initialTitle = detail?.title.orEmpty(),
			initialDescription = detail?.description.orEmpty(),
		)
	val colorState =
		rememberDiaryColorState(
			inputs = arrayOf(detail?.color),
			initialColor = detail?.color?.let { Color(it) } ?: Color.Unspecified,
		)

	return remember(componentState, colorState) {
		TagDetailScaffoldState.Detail(
			onUpdate = onUpdate,
			onDelete = onDelete,
			navigateToMemo = navigateToMemo,
			coroutineScope = coroutineScope,
			componentState = componentState,
			colorState = colorState,
		)
	}
}
