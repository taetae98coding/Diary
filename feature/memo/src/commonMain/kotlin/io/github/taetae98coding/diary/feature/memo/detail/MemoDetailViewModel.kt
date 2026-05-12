@file:OptIn(ExperimentalCoroutinesApi::class)

package io.github.taetae98coding.diary.feature.memo.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.taetae98coding.diary.core.model.memo.Memo
import io.github.taetae98coding.diary.core.model.memo.MemoDetail
import io.github.taetae98coding.diary.core.navigation.argument.MemoId
import io.github.taetae98coding.diary.domain.memo.usecase.CopyMemoUseCase
import io.github.taetae98coding.diary.domain.memo.usecase.DeleteMemoUseCase
import io.github.taetae98coding.diary.domain.memo.usecase.FinishMemoUseCase
import io.github.taetae98coding.diary.domain.memo.usecase.GetMemoTagUseCase
import io.github.taetae98coding.diary.domain.memo.usecase.GetMemoUseCase
import io.github.taetae98coding.diary.domain.memo.usecase.RestartMemoUseCase
import io.github.taetae98coding.diary.domain.memo.usecase.UpdateMemoUseCase
import io.github.taetae98coding.diary.feature.memo.common.MemoTagUiState
import io.github.taetae98coding.diary.feature.memo.common.TagCardUiState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.annotation.InjectedParam
import org.koin.core.annotation.KoinViewModel

@KoinViewModel
internal class MemoDetailViewModel(
    @param:InjectedParam
    private val memoId: MemoId,
    getMemoUseCase: GetMemoUseCase,
    getMemoTagUseCase: GetMemoTagUseCase,
    private val copyMemoUseCase: CopyMemoUseCase,
    private val updateMemoUseCase: UpdateMemoUseCase,
    private val finishMemoUseCase: FinishMemoUseCase,
    private val restartMemoUseCase: RestartMemoUseCase,
    private val deleteMemoUseCase: DeleteMemoUseCase,
) : ViewModel() {
    val memo: StateFlow<Memo?> = getMemoUseCase(memoId.value).mapLatest { it.getOrNull() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = null,
        )

    val tagCardUiState = combine(
        memo,
        getMemoTagUseCase(memoId.value).mapLatest { it.getOrDefault(emptyList()) },
    ) { memo, tagList ->
        TagCardUiState.State(
            tagList = tagList.map { tag ->
                MemoTagUiState(
                    tagId = tag.id,
                    title = tag.detail.title,
                    color = tag.detail.color,
                    isSelected = true,
                    isPrimary = tag.id == memo?.primaryTag,
                )
            },
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = TagCardUiState.Loading,
    )

    private val _updateInProgress = MutableStateFlow(false)
    val updateInProgress = _updateInProgress.asStateFlow()

    private val finishInProgress = MutableStateFlow(false)
    val finishUiState = combine(memo, finishInProgress) { memo, inProgress ->
        MemoDetailFinishUiState(
            isFinished = memo?.isFinished ?: false,
            isInProgress = inProgress,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = MemoDetailFinishUiState(),
    )

    private val _copyUiState = MutableStateFlow(MemoDetailCopyUiState())
    val copyUiState = _copyUiState.asStateFlow()

    private val _deleteUiState = MutableStateFlow(MemoDetailDeleteUiState())
    val deleteUiState = _deleteUiState.asStateFlow()

    private val _effect = MutableStateFlow<MemoDetailEffect>(MemoDetailEffect.None)
    val effect = _effect.asStateFlow()

    fun update(detail: MemoDetail) {
        if (_updateInProgress.value) return

        viewModelScope.launch {
            _updateInProgress.value = true
            updateMemoUseCase(memoId.value, detail)
                .onSuccess { _effect.value = MemoDetailEffect.UpdateFinish }
                .onFailure { _effect.value = MemoDetailEffect.UnknownError }
            _updateInProgress.value = false
        }
    }

    fun finish() {
        if (finishInProgress.value) return

        viewModelScope.launch {
            finishInProgress.value = true
            finishMemoUseCase(memoId.value)
                .onFailure { _effect.value = MemoDetailEffect.UnknownError }
            finishInProgress.value = false
        }
    }

    fun restart() {
        if (finishInProgress.value) return

        viewModelScope.launch {
            finishInProgress.value = true
            restartMemoUseCase(memoId.value)
                .onFailure { _effect.value = MemoDetailEffect.UnknownError }
            finishInProgress.value = false
        }
    }

    fun copy() {
        if (_copyUiState.value.isInProgress) return

        viewModelScope.launch {
            _copyUiState.update { it.copy(isInProgress = true) }
            copyMemoUseCase(memoId.value)
                .onSuccess { _effect.value = MemoDetailEffect.CopyFinish }
                .onFailure { _effect.value = MemoDetailEffect.UnknownError }
            _copyUiState.update { it.copy(isInProgress = false) }
        }
    }

    fun delete() {
        if (_deleteUiState.value.isInProgress) return

        viewModelScope.launch {
            _deleteUiState.update { it.copy(isInProgress = true) }
            deleteMemoUseCase(memoId.value)
                .onSuccess { _effect.value = MemoDetailEffect.DeleteFinish }
                .onFailure { _effect.value = MemoDetailEffect.UnknownError }
            _deleteUiState.update { it.copy(isInProgress = false) }
        }
    }

    fun consumeEffect() {
        _effect.value = MemoDetailEffect.None
    }
}
