package com.taetae98.diary.feature.tag.detail

import com.taetae98.diary.domain.entity.tag.Tag
import com.taetae98.diary.domain.entity.tag.TagId
import com.taetae98.diary.domain.usecase.tag.FindTagByIdUseCase
import com.taetae98.diary.domain.usecase.tag.UpsertTagUseCase
import com.taetae98.diary.library.viewmodel.SavedStateHandle
import com.taetae98.diary.library.viewmodel.ViewModel
import com.taetae98.diary.navigation.core.tag.TagDetailEntry
import com.taetae98.diary.ui.compose.text.TextFieldUiStateHolder
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.koin.core.annotation.Factory

@OptIn(ExperimentalCoroutinesApi::class)
@Factory
internal class TagDetailViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val findTagByIdUseCase: FindTagByIdUseCase,
    private val upsertTagUseCase: UpsertTagUseCase,
) : ViewModel() {
    private val isChanged = savedStateHandle.getStateFlow(
        key = CHANGED,
        initialValue = false,
    )

    private val tagId = savedStateHandle.getStateFlow(
        key = TagDetailEntry.ID,
        initialValue = "",
    )

    private val tag = tagId.flatMapLatest { findTagByIdUseCase(TagId(it)) }
        .mapLatest(Result<Tag?>::getOrNull)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = null,
        )

    private val _message = MutableStateFlow<TagDetailMessage?>(null)
    val message = _message.asStateFlow()

    val uiState = TagDetailUiState(onUpsert = ::upsert)

    val titleUiStateHolder = TextFieldUiStateHolder(
        scope = viewModelScope,
        key = TITLE,
        initialValue = "",
        savedStateHandle = savedStateHandle,
        onValueChange = { savedStateHandle[CHANGED] = true },
    )
    val descriptionUiStateHolder = TextFieldUiStateHolder(
        scope = viewModelScope,
        key = DESCRIPTION,
        initialValue = "",
        savedStateHandle = savedStateHandle,
        onValueChange = { savedStateHandle[CHANGED] = true },
    )

    init {
        viewModelScope.launch {
            launch { collectTagAndUpdate() }
        }
    }

    private suspend fun collectTagAndUpdate() {
        tag.distinctUntilChangedBy { it?.id }
            .collectLatest {
                titleUiStateHolder.setValue(it?.title.orEmpty())
                descriptionUiStateHolder.setValue(it?.description.orEmpty())
            }
    }

    private fun upsert() {
        if (!isChanged.value) {
            viewModelScope.launch {
                _message.emit(TagDetailMessage.Upsert(::messageShown))
            }

            return
        }

        val tag = tag.value?.copy(
            title = titleUiStateHolder.getValue().value,
            description = descriptionUiStateHolder.getValue().value,
        ) ?: return

        viewModelScope.launch {
            upsertTagUseCase(tag).onSuccess {
                savedStateHandle[CHANGED] = false
                _message.emit(TagDetailMessage.Upsert(::messageShown))
            }.onFailure {
                _message.emit(TagDetailMessage.UpsertFail(::messageShown))
            }
        }
    }

    private fun messageShown() {
        viewModelScope.launch {
            _message.emit(null)
        }
    }

    companion object {
        private const val CHANGED = "changed"
        private const val TITLE = "title"
        private const val DESCRIPTION = "description"
    }
}