package io.github.taetae98coding.diary.feature.tag.add

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import io.github.taetae98coding.diary.core.design.system.diary.color.rememberDiaryColorState
import io.github.taetae98coding.diary.core.design.system.diary.component.rememberDiaryComponentState
import io.github.taetae98coding.diary.feature.tag.detail.TagDetailScreenState

@Composable
internal fun rememberTagDetailScreenAddState(): TagDetailScreenState.Add {
	val coroutineScope = rememberCoroutineScope()
	val componentState = rememberDiaryComponentState()
	val colorState = rememberDiaryColorState()

	return remember {
		TagDetailScreenState.Add(
			coroutineScope = coroutineScope,
			componentState = componentState,
			colorState = colorState,
		)
	}
}
