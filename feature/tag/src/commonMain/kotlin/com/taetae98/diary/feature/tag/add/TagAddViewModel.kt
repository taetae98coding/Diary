package com.taetae98.diary.feature.tag.add

import com.taetae98.diary.domain.entity.tag.Tag
import com.taetae98.diary.domain.entity.tag.TagState
import com.taetae98.diary.domain.usecase.account.GetAccountUseCase
import com.taetae98.diary.domain.usecase.tag.UpsertTagUseCase
import com.taetae98.diary.feature.tag.detail.TagDetailMessage
import com.taetae98.diary.feature.tag.detail.TagDetailUiState
import com.taetae98.diary.library.uuid.getUuid
import com.taetae98.diary.library.viewmodel.SavedStateHandle
import com.taetae98.diary.library.viewmodel.ViewModel
import com.taetae98.diary.ui.compose.text.TextFieldUiStateHolder
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.koin.core.annotation.Factory

@Factory
internal class TagAddViewModel(
    savedStateHandle: SavedStateHandle,
    private val upsertTagUseCase: UpsertTagUseCase,
    private val getAccountUseCase: GetAccountUseCase,
) : ViewModel() {
    private val message = MutableStateFlow<TagDetailMessage?>(null)

    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState = message.mapLatest {
        TagDetailUiState.Add(
            onAdd = ::upsert,
            message = it,
            onMessageShown = ::messageShown
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = TagDetailUiState.Add(
            onAdd = ::upsert,
            message = message.value,
            onMessageShown = ::messageShown
        )
    )

    val titleUiStateHolder = TextFieldUiStateHolder(
        scope = viewModelScope,
        key = TITLE,
        initialValue = "",
        savedStateHandle = savedStateHandle,
    )
    val descriptionUiStateHolder = TextFieldUiStateHolder(
        scope = viewModelScope,
        key = DESCRIPTION,
        initialValue = "",
        savedStateHandle = savedStateHandle,
    )

    private fun upsert() {
        viewModelScope.launch {
            val tag = Tag(
                id = getUuid(),
                title = titleUiStateHolder.getValue().value,
                description = descriptionUiStateHolder.getValue().value,
                state = TagState.NONE,
                ownerId = getAccountUseCase(Unit).firstOrNull()?.getOrNull()?.uid,
            )

            upsertTagUseCase(tag).onSuccess {
                clearInput()
                showAddMessage()
            }
        }
    }

    private fun clearInput() {
        titleUiStateHolder.setValue("")
        descriptionUiStateHolder.setValue("")
    }

    private fun showAddMessage() {
        viewModelScope.launch {
            message.emit(TagDetailMessage.Add)
        }
    }

    private fun messageShown() {
        viewModelScope.launch {
            message.emit(null)
        }
    }

    companion object {
        private const val TITLE = "title"
        private const val DESCRIPTION = "description"
    }
}