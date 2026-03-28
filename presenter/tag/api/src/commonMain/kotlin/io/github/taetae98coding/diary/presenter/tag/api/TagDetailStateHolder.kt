package io.github.taetae98coding.diary.presenter.tag.api

import io.github.taetae98coding.diary.core.model.tag.Tag
import io.github.taetae98coding.diary.core.model.tag.TagDetail
import io.github.taetae98coding.diary.core.navigation.argument.TagId
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

public class TagDetailStateHolder(
    private val coroutineScope: CoroutineScope,
    private val tagId: TagId,
    private val strategy: TagDetailStrategy,
) {
    public val tag: StateFlow<Tag?> = strategy.get(tagId.value).mapLatest { it.getOrNull() }
        .stateIn(
            scope = coroutineScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = null,
        )

    private val _updateInProgress: MutableStateFlow<Boolean> = MutableStateFlow(false)
    public val updateInProgress: StateFlow<Boolean> = _updateInProgress.asStateFlow()

    private val finishInProgress: MutableStateFlow<Boolean> = MutableStateFlow(false)
    public val finishUiState: StateFlow<TagDetailFinishUiState> = combine(tag, finishInProgress) { tag, inProgress ->
        TagDetailFinishUiState(
            isFinished = tag?.isFinished ?: false,
            isInProgress = inProgress,
        )
    }.stateIn(
        scope = coroutineScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = TagDetailFinishUiState(),
    )

    private val _deleteUiState: MutableStateFlow<TagDetailDeleteUiState> = MutableStateFlow(TagDetailDeleteUiState())
    public val deleteUiState: StateFlow<TagDetailDeleteUiState> = _deleteUiState.asStateFlow()

    private val _effect: MutableStateFlow<TagDetailEffect> = MutableStateFlow(TagDetailEffect.None)
    public val effect: StateFlow<TagDetailEffect> = _effect.asStateFlow()

    public fun update(detail: TagDetail) {
        if (_updateInProgress.value) return

        coroutineScope.launch {
            _updateInProgress.value = true
            strategy.update(tagId.value, detail)
                .onSuccess { _effect.value = TagDetailEffect.UpdateFinish }
                .onFailure { _effect.value = TagDetailEffect.UnknownError }
            _updateInProgress.value = false
        }
    }

    public fun finish() {
        if (finishInProgress.value) return

        coroutineScope.launch {
            finishInProgress.value = true
            strategy.finish(tagId.value)
                .onFailure { _effect.value = TagDetailEffect.UnknownError }
            finishInProgress.value = false
        }
    }

    public fun restart() {
        if (finishInProgress.value) return

        coroutineScope.launch {
            finishInProgress.value = true
            strategy.restart(tagId.value)
                .onFailure { _effect.value = TagDetailEffect.UnknownError }
            finishInProgress.value = false
        }
    }

    public fun delete() {
        if (_deleteUiState.value.isInProgress) return

        coroutineScope.launch {
            _deleteUiState.update { it.copy(isInProgress = true) }
            strategy.delete(tagId.value)
                .onSuccess { _effect.value = TagDetailEffect.DeleteFinish }
                .onFailure { _effect.value = TagDetailEffect.UnknownError }
            _deleteUiState.update { it.copy(isInProgress = false) }
        }
    }

    public fun consumeEffect() {
        _effect.value = TagDetailEffect.None
    }
}
