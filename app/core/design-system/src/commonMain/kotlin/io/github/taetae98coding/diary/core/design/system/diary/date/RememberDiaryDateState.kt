package io.github.taetae98coding.diary.core.design.system.diary.date

import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.rememberSaveable
import kotlinx.datetime.LocalDate

@Composable
public fun rememberDiaryDateState(
	vararg inputs: Any?,
	initialStart: LocalDate?,
	initialEndInclusive: LocalDate?,
): DiaryDateState =
	rememberSaveable(
		inputs = inputs,
		saver = DiaryDateState.saver(),
	) {
		DiaryDateState(
			initialStart = initialStart,
			initialEndInclusive = initialEndInclusive,
		)
	}
