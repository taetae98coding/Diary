package io.github.taetae98coding.diary.feature.tag.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.taetae98coding.diary.core.model.tag.Tag
import io.github.taetae98coding.diary.core.model.tag.TagDetail
import io.github.taetae98coding.diary.core.navigation.argument.TagId
import io.github.taetae98coding.diary.domain.tag.usecase.DeleteTagUseCase
import io.github.taetae98coding.diary.domain.tag.usecase.FinishTagUseCase
import io.github.taetae98coding.diary.domain.tag.usecase.GetTagUseCase
import io.github.taetae98coding.diary.domain.tag.usecase.RestartTagUseCase
import io.github.taetae98coding.diary.domain.tag.usecase.UpdateTagUseCase
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
internal class TagDetailViewModel(
    @param:InjectedParam
    private val tagId: TagId,
    getTagUseCase: GetTagUseCase,
    private val updateTagUseCase: UpdateTagUseCase,
    private val finishTagUseCase: FinishTagUseCase,
    private val restartTagUseCase: RestartTagUseCase,
    private val deleteTagUseCase: DeleteTagUseCase,
) : ViewModel() {
    val tag: StateFlow<Tag?> = getTagUseCase(tagId.value).mapLatest { it.getOrNull() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = null,
        )

    private val _updateInProgress = MutableStateFlow(false)
    val updateInProgress = _updateInProgress.asStateFlow()

    private val finishInProgress = MutableStateFlow(false)
    val finishUiState = combine(tag, finishInProgress) { tag, inProgress ->
        TagDetailFinishUiState(
            isFinished = tag?.isFinished ?: false,
            isInProgress = inProgress,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = TagDetailFinishUiState(),
    )

    private val _deleteUiState = MutableStateFlow(TagDetailDeleteUiState())
    val deleteUiState = _deleteUiState.asStateFlow()

    private val _effect = MutableStateFlow<TagDetailEffect>(TagDetailEffect.None)
    val effect = _effect.asStateFlow()

    fun update(detail: TagDetail) {
        if (_updateInProgress.value) return

        viewModelScope.launch {
            _updateInProgress.value = true
            updateTagUseCase(tagId.value, detail)
                .onSuccess { _effect.value = TagDetailEffect.UpdateFinish }
                .onFailure { _effect.value = TagDetailEffect.UnknownError }
            _updateInProgress.value = false
        }
    }

    fun finish() {
        if (finishInProgress.value) return

        viewModelScope.launch {
            finishInProgress.value = true
            finishTagUseCase(tagId.value)
                .onFailure { _effect.value = TagDetailEffect.UnknownError }
            finishInProgress.value = false
        }
    }

    fun restart() {
        if (finishInProgress.value) return

        viewModelScope.launch {
            finishInProgress.value = true
            restartTagUseCase(tagId.value)
                .onFailure { _effect.value = TagDetailEffect.UnknownError }
            finishInProgress.value = false
        }
    }

    fun delete() {
        if (_deleteUiState.value.isInProgress) return

        viewModelScope.launch {
            _deleteUiState.update { it.copy(isInProgress = true) }
            deleteTagUseCase(tagId.value)
                .onSuccess { _effect.value = TagDetailEffect.DeleteFinish }
                .onFailure { _effect.value = TagDetailEffect.UnknownError }
            _deleteUiState.update { it.copy(isInProgress = false) }
        }
    }

    fun consumeEffect() {
        _effect.value = TagDetailEffect.None
    }
}
