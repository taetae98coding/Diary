package io.github.taetae98coding.diary.presenter.memo.api

import io.github.taetae98coding.diary.core.model.memo.Memo
import io.github.taetae98coding.diary.core.model.memo.MemoDetail
import io.github.taetae98coding.diary.core.navigation.argument.MemoId
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

public class MemoDetailStateHolder(
    private val coroutineScope: CoroutineScope,
    private val memoId: MemoId,
    private val strategy: MemoDetailStrategy,
) {
    public val memo: StateFlow<Memo?> = strategy.get(memoId.value).mapLatest { it.getOrNull() }
        .stateIn(
            scope = coroutineScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = null,
        )

    public val tagCardUiState: StateFlow<TagCardUiState> = combine(
        memo,
        strategy.getMemoTag(memoId.value).mapLatest { it.getOrDefault(emptyList()) },
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
        scope = coroutineScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = TagCardUiState.Loading,
    )

    private val _updateInProgress: MutableStateFlow<Boolean> = MutableStateFlow(false)
    public val updateInProgress: StateFlow<Boolean> = _updateInProgress.asStateFlow()

    private val finishInProgress: MutableStateFlow<Boolean> = MutableStateFlow(false)
    public val finishUiState: StateFlow<MemoDetailFinishUiState> = combine(memo, finishInProgress) { memo, inProgress ->
        MemoDetailFinishUiState(
            isFinished = memo?.isFinished ?: false,
            isInProgress = inProgress,
        )
    }.stateIn(
        scope = coroutineScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = MemoDetailFinishUiState(),
    )

    private val _deleteUiState: MutableStateFlow<MemoDetailDeleteUiState> = MutableStateFlow(MemoDetailDeleteUiState())
    public val deleteUiState: StateFlow<MemoDetailDeleteUiState> = _deleteUiState.asStateFlow()

    private val _effect: MutableStateFlow<MemoDetailEffect> = MutableStateFlow(MemoDetailEffect.None)
    public val effect: StateFlow<MemoDetailEffect> = _effect.asStateFlow()

    public fun update(detail: MemoDetail) {
        if (_updateInProgress.value) return

        coroutineScope.launch {
            _updateInProgress.value = true
            strategy.update(memoId.value, detail)
                .onSuccess { _effect.value = MemoDetailEffect.UpdateFinish }
                .onFailure { _effect.value = MemoDetailEffect.UnknownError }
            _updateInProgress.value = false
        }
    }

    public fun finish() {
        if (finishInProgress.value) return

        coroutineScope.launch {
            finishInProgress.value = true
            strategy.finish(memoId.value)
                .onFailure { _effect.value = MemoDetailEffect.UnknownError }
            finishInProgress.value = false
        }
    }

    public fun restart() {
        if (finishInProgress.value) return

        coroutineScope.launch {
            finishInProgress.value = true
            strategy.restart(memoId.value)
                .onFailure { _effect.value = MemoDetailEffect.UnknownError }
            finishInProgress.value = false
        }
    }

    public fun delete() {
        if (_deleteUiState.value.isInProgress) return

        coroutineScope.launch {
            _deleteUiState.update { it.copy(isInProgress = true) }
            strategy.delete(memoId.value)
                .onSuccess { _effect.value = MemoDetailEffect.DeleteFinish }
                .onFailure { _effect.value = MemoDetailEffect.UnknownError }
            _deleteUiState.update { it.copy(isInProgress = false) }
        }
    }

    public fun consumeEffect() {
        _effect.value = MemoDetailEffect.None
    }
}
