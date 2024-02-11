package com.taetae98.diary.feature.tag.detail

import com.taetae98.diary.domain.entity.tag.Tag
import com.taetae98.diary.domain.usecase.tag.FindTagByIdUseCase
import com.taetae98.diary.library.viewmodel.SavedStateHandle
import com.taetae98.diary.library.viewmodel.ViewModel
import com.taetae98.diary.navigation.core.tag.TagDetailEntry
import com.taetae98.diary.ui.compose.text.TextFieldUiStateHolder
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import org.koin.core.annotation.Factory

@Factory
internal class TagDetailViewModel(
    savedStateHandle: SavedStateHandle,
    private val findTagByIdUseCase: FindTagByIdUseCase,
) : ViewModel() {
    private val tagId = savedStateHandle.getStateFlow(
        key = TagDetailEntry.ID,
        initialValue = ""
    )

    @OptIn(ExperimentalCoroutinesApi::class)
    private val tag = tagId.flatMapLatest { findTagByIdUseCase(it) }
        .mapLatest(Result<Tag?>::getOrNull)
        .onEach(::onTagChanged)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = null,
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

    private fun onTagChanged(tag: Tag?) {
        titleUiStateHolder.setValue(tag?.title.orEmpty())
        descriptionUiStateHolder.setValue(tag?.description.orEmpty())
    }

    companion object {
        private const val TITLE = "title"
        private const val DESCRIPTION = "description"
    }
}