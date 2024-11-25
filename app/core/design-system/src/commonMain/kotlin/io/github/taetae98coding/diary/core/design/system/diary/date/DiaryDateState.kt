package io.github.taetae98coding.diary.core.design.system.diary.date

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.setValue
import io.github.taetae98coding.diary.library.datetime.todayIn
import kotlinx.datetime.LocalDate

public class DiaryDateState internal constructor(initialStart: LocalDate?, initialEndInclusive: LocalDate?) : ClosedRange<LocalDate> {
	public var hasDate: Boolean by mutableStateOf(initialStart != null && initialEndInclusive != null)
		private set
	override var start: LocalDate by mutableStateOf(initialStart ?: LocalDate.todayIn())
		private set
	override var endInclusive: LocalDate by mutableStateOf(initialEndInclusive ?: LocalDate.todayIn())
		private set

	internal fun onHasDateChange(value: Boolean) {
		hasDate = value
	}

	internal fun onStartChange(value: LocalDate) {
		start = value
		if (endInclusive < value) {
			endInclusive = value
		}
	}

	internal fun onEndInclusiveChange(value: LocalDate) {
		endInclusive = value
		if (start > value) {
			start = value
		}
	}

	public companion object {
		internal fun saver(): Saver<DiaryDateState, Any> =
			listSaver(
				save = { listOf(it.hasDate, it.start.toEpochDays(), it.endInclusive.toEpochDays()) },
				restore = {
					DiaryDateState(
						initialStart = LocalDate.fromEpochDays(it[1] as Int),
						initialEndInclusive = LocalDate.fromEpochDays(it[2] as Int),
					).apply {
						hasDate = it[0] as Boolean
					}
				},
			)
	}
}
