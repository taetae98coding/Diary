package io.github.taetae98coding.diary.core.compose.tag.add

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import io.github.taetae98coding.diary.core.compose.tag.detail.TagDetailScaffoldState
import io.github.taetae98coding.diary.core.design.system.diary.color.rememberDiaryColorState
import io.github.taetae98coding.diary.core.design.system.diary.component.rememberDiaryComponentState

@Composable
public fun rememberTagDetailScaffoldAddState(): TagDetailScaffoldState.Add {
	val coroutineScope = rememberCoroutineScope()
	val componentState = rememberDiaryComponentState()
	val colorState = rememberDiaryColorState()

	return remember(componentState, colorState) {
		TagDetailScaffoldState.Add(
			coroutineScope = coroutineScope,
			componentState = componentState,
			colorState = colorState,
		)
	}
}
