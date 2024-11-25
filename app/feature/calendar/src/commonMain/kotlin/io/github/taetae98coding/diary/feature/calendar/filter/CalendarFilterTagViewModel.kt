package io.github.taetae98coding.diary.feature.calendar.filter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.taetae98coding.diary.core.compose.runtime.SkipProperty
import io.github.taetae98coding.diary.domain.calendar.usecase.DeleteCalendarTagUseCase
import io.github.taetae98coding.diary.domain.calendar.usecase.FindCalendarTagFilterUseCase
import io.github.taetae98coding.diary.domain.calendar.usecase.UpsertCalendarTagUseCase
import io.github.taetae98coding.diary.library.coroutines.mapCollectionLatest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@OptIn(ExperimentalCoroutinesApi::class)
@KoinViewModel
internal class CalendarFilterTagViewModel(
	findCalendarTagFilterUseCase: FindCalendarTagFilterUseCase,
	private val upsertCalendarTagUseCase: UpsertCalendarTagUseCase,
	private val deleteCalendarTagUseCase: DeleteCalendarTagUseCase,
) : ViewModel() {
	val list =
		findCalendarTagFilterUseCase()
			.mapLatest { it.getOrNull() }
			.mapCollectionLatest {
				TagUiState(
					id = it.tag.id,
					title = it.tag.detail.title,
					isSelected = it.isSelected,
					select = SkipProperty { select(it.tag.id) },
					unselect = SkipProperty { unselect(it.tag.id) },
				)
			}.stateIn(
				scope = viewModelScope,
				started = SharingStarted.WhileSubscribed(5_000),
				initialValue = null,
			)

	private fun select(tagId: String) {
		viewModelScope.launch {
			upsertCalendarTagUseCase(tagId)
		}
	}

	private fun unselect(tagId: String) {
		viewModelScope.launch {
			deleteCalendarTagUseCase(tagId)
		}
	}
}
