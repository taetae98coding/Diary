package com.taetae98.diary.feature.tag.add

import com.taetae98.diary.domain.entity.tag.Tag
import com.taetae98.diary.domain.exception.TitleEmptyException
import com.taetae98.diary.domain.usecase.account.GetAccountUseCase
import com.taetae98.diary.domain.usecase.tag.UpsertTagUseCase
import com.taetae98.diary.library.uuid.getUuid
import com.taetae98.diary.library.viewmodel.SavedStateHandle
import com.taetae98.diary.library.viewmodel.ViewModel
import com.taetae98.diary.ui.compose.text.TextFieldUiStateHolder
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import org.koin.core.annotation.Factory

@Factory
internal class TagAddViewModel(
    savedStateHandle: SavedStateHandle,
    private val upsertTagUseCase: UpsertTagUseCase,
    private val getAccountUseCase: GetAccountUseCase,
) : ViewModel() {
    private val _message = MutableStateFlow<TagAddMessage?>(null)
    val message = _message.asStateFlow()

    val uiState = TagAddUiState(
        onAdd = ::upsert,
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
                ownerId = getAccountUseCase(Unit).firstOrNull()?.getOrNull()?.uid,
            )

            upsertTagUseCase(tag).onSuccess {
                clearInput()
                _message.emit(TagAddMessage.Add(::messageShown))
            }.onFailure {
                when (it) {
                    is TitleEmptyException -> _message.emit(TagAddMessage.TitleEmpty(::messageShown))
                }
            }
        }
    }

    private fun clearInput() {
        titleUiStateHolder.setValue("")
        descriptionUiStateHolder.setValue("")
    }

    private fun messageShown() {
        viewModelScope.launch {
            _message.emit(null)
        }
    }

    companion object {
        private const val TITLE = "title"
        private const val DESCRIPTION = "description"
    }
}