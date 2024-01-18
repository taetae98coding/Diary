package com.taetae98.diary.feature.tag.add

import com.taetae98.diary.library.viewmodel.SavedStateHandle
import com.taetae98.diary.library.viewmodel.ViewModel
import com.taetae98.diary.ui.compose.text.TextFieldUiStateHolder
import org.koin.core.annotation.Factory

@Factory
internal class TagAddViewModel(
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    val titleUiStateHolder = TextFieldUiStateHolder(
        scope = viewModelScope,
        key = TITLE,
        initialValue = "",
        savedStateHandle = savedStateHandle,
    )

    companion object {
        private const val TITLE = "title"
    }
}