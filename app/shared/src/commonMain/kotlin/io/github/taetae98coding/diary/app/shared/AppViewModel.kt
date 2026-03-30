package io.github.taetae98coding.diary.app.shared

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.taetae98coding.diary.core.model.sync.SyncType
import io.github.taetae98coding.diary.domain.sync.usecase.RequestSyncUseCase
import kotlinx.coroutines.launch
import org.koin.core.annotation.KoinViewModel

@KoinViewModel
internal class AppViewModel(private val requestSyncUseCase: RequestSyncUseCase) : ViewModel() {
    fun sync() {
        viewModelScope.launch {
            requestSyncUseCase(SyncType.Foreground)
        }
    }
}
