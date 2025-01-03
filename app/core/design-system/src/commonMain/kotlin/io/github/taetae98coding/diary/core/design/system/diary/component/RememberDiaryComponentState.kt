package io.github.taetae98coding.diary.core.design.system.diary.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.rememberSaveable

@Composable
public fun rememberDiaryComponentState(
	vararg inputs: Any?,
	initialTitle: String = "",
	initialDescription: String = "",
): DiaryComponentState =
	rememberSaveable(
		inputs = inputs,
		saver = DiaryComponentState.saver(),
	) {
		DiaryComponentState(
			initialTitle = initialTitle,
			initialDescription = initialDescription,
		)
	}
