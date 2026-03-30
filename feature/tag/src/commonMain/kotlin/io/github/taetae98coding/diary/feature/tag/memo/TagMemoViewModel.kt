package io.github.taetae98coding.diary.feature.tag.memo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.taetae98coding.diary.core.model.sync.SyncStatus
import io.github.taetae98coding.diary.core.model.sync.SyncType
import io.github.taetae98coding.diary.core.navigation.argument.TagId
import io.github.taetae98coding.diary.domain.sync.usecase.GetSyncStatusUseCase
import io.github.taetae98coding.diary.domain.sync.usecase.RequestSyncUseCase
import io.github.taetae98coding.diary.domain.tag.usecase.GetTagUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.koin.core.annotation.KoinViewModel

@KoinViewModel
internal class TagMemoViewModel(
    tagId: TagId,
    getTagUseCase: GetTagUseCase,
    getSyncStatusUseCase: GetSyncStatusUseCase,
    private val requestSyncUseCase: RequestSyncUseCase,
) : ViewModel() {
    val title = getTagUseCase(tagId.value)
        .mapLatest { it.getOrNull()?.detail?.title.orEmpty() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = "",
        )

    val isSyncing = getSyncStatusUseCase().mapLatest { it.getOrNull() }
        .map { it is SyncStatus.Syncing && it.type == SyncType.Foreground }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = false,
        )

    fun sync() {
        viewModelScope.launch { requestSyncUseCase(SyncType.Foreground) }
    }
}
