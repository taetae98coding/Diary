package io.github.taetae98coding.diary.feature.tag.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.taetae98coding.diary.common.exception.ext.isNetworkException
import io.github.taetae98coding.diary.core.compose.runtime.SkipProperty
import io.github.taetae98coding.diary.core.compose.tag.list.TagListItemUiState
import io.github.taetae98coding.diary.core.compose.tag.list.TagListScaffoldUiState
import io.github.taetae98coding.diary.core.compose.tag.list.TagListUiState
import io.github.taetae98coding.diary.domain.tag.usecase.DeleteTagUseCase
import io.github.taetae98coding.diary.domain.tag.usecase.FinishTagUseCase
import io.github.taetae98coding.diary.domain.tag.usecase.PageTagUseCase
import io.github.taetae98coding.diary.domain.tag.usecase.RestartTagUseCase
import io.github.taetae98coding.diary.domain.tag.usecase.RestoreTagUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@OptIn(ExperimentalCoroutinesApi::class)
@KoinViewModel
internal class TagListViewModel(
    pageTagUseCase: PageTagUseCase,
    private val finishTagUseCase: FinishTagUseCase,
    private val deleteTagUseCase: DeleteTagUseCase,
    private val restartTagUseCase: RestartTagUseCase,
    private val restoreTagUseCase: RestoreTagUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(TagListScaffoldUiState(clearState = ::clearState))
    val uiState = _uiState.asStateFlow()

    private val refreshFlow = MutableStateFlow(0)
    val list = refreshFlow.flatMapLatest { pageTagUseCase() }
        .mapLatest { result ->
            if (result.isSuccess) {
                TagListUiState.State(
                    list = result.getOrNull()
                        .orEmpty()
                        .map {
                            TagListItemUiState(
                                id = it.id,
                                title = it.detail.title,
                                finish = SkipProperty { finish(it.id) },
                                delete = SkipProperty { delete(it.id) },
                            )
                        },
                )
            } else {
                when (val exception = result.exceptionOrNull()) {
                    is Exception if exception.isNetworkException() -> TagListUiState.NetworkError(::refresh)
                    else -> TagListUiState.UnknownError(::refresh)
                }
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = TagListUiState.Loading,
        )

    private fun finish(tagId: String) {
        viewModelScope.launch {
            finishTagUseCase(tagId)
                .onSuccess {
                    _uiState.update {
                        it.copy(
                            isTagFinish = true,
                            restart = SkipProperty { restart(tagId) },
                        )
                    }
                }
                .onFailure { _uiState.update { it.copy(isUnknownError = true) } }
        }
    }

    private fun delete(tagId: String) {
        viewModelScope.launch {
            deleteTagUseCase(tagId)
                .onSuccess {
                    _uiState.update {
                        it.copy(
                            isTagDelete = true,
                            restore = SkipProperty { restore(tagId) },
                        )
                    }
                }
                .onFailure { _uiState.update { it.copy(isUnknownError = true) } }
        }
    }

    private fun restart(id: String) {
        viewModelScope.launch {
            restartTagUseCase(id)
        }
    }

    private fun restore(id: String) {
        viewModelScope.launch {
            restoreTagUseCase(id)
        }
    }

    private fun clearState() {
        _uiState.update {
            it.copy(
                isTagFinish = false,
                isTagDelete = false,
                isUnknownError = false,
                restart = SkipProperty {},
                restore = SkipProperty {},
            )
        }
    }

    private fun refresh() {
        viewModelScope.launch {
            refreshFlow.emit(refreshFlow.value + 1)
        }
    }
}
