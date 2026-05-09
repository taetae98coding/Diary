@file:OptIn(ExperimentalCoroutinesApi::class)

package io.github.taetae98coding.diary.feature.memo.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.taetae98coding.diary.core.model.sync.SyncStatus
import io.github.taetae98coding.diary.core.model.sync.SyncType
import io.github.taetae98coding.diary.domain.sync.usecase.GetSyncStatusUseCase
import io.github.taetae98coding.diary.domain.sync.usecase.RequestSyncUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.koin.core.annotation.KoinViewModel

@KoinViewModel
internal class MemoHomeViewModel(
    getSyncStatusUseCase: GetSyncStatusUseCase,
    private val requestSyncUseCase: RequestSyncUseCase,
) : ViewModel() {
    private val syncStatus = getSyncStatusUseCase().mapLatest { it.getOrNull() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = null,
        )

    val isSyncing: StateFlow<Boolean> = syncStatus
        .map { it is SyncStatus.Syncing && it.type == SyncType.Foreground }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = false,
        )

    val isFailed: StateFlow<Boolean> = syncStatus
        .map { it is SyncStatus.Failed }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = false,
        )

    fun sync() {
        viewModelScope.launch { requestSyncUseCase(SyncType.Foreground) }
    }
}
