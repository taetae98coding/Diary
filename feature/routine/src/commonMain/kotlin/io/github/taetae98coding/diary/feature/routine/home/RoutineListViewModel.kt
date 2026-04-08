package io.github.taetae98coding.diary.feature.routine.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import io.github.taetae98coding.diary.core.model.routine.Routine
import io.github.taetae98coding.diary.domain.routine.usecase.PageRoutineUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest
import org.koin.core.annotation.KoinViewModel

@KoinViewModel
internal class RoutineListViewModel(pageRoutineUseCase: PageRoutineUseCase) : ViewModel() {
    val pagingData: Flow<PagingData<Routine>> = pageRoutineUseCase()
        .mapLatest { it.getOrDefault(PagingData.empty()) }
        .cachedIn(viewModelScope)
}
