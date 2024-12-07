package io.github.taetae98coding.diary.feature.buddy.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.taetae98coding.diary.domain.buddy.usecase.FindBuddyGroupUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import org.koin.android.annotation.KoinViewModel

@OptIn(ExperimentalCoroutinesApi::class)
@KoinViewModel
internal class BuddyListViewModel(
	findBuddyGroupUseCase: FindBuddyGroupUseCase,
) : ViewModel() {
	val groupList = findBuddyGroupUseCase()
		.mapLatest { it.getOrNull() }
		.stateIn(
			scope = viewModelScope,
			started = SharingStarted.WhileSubscribed(5_000),
			initialValue = null,
		)
}
