package io.github.taetae98coding.diary.feature.calendar.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.taetae98coding.diary.domain.sync.usecase.SyncUseCase
import kotlinx.coroutines.launch
import org.koin.core.annotation.KoinViewModel

@KoinViewModel
internal class CalendarHomeViewModel(private val syncUseCase: SyncUseCase) : ViewModel() {
    fun sync() {
        viewModelScope.launch {
            syncUseCase()
        }
    }
}
