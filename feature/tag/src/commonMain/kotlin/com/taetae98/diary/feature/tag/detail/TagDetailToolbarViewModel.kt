package com.taetae98.diary.feature.tag.detail

import com.taetae98.diary.domain.entity.tag.TagId
import com.taetae98.diary.domain.usecase.tag.DeleteTagUseCase
import com.taetae98.diary.library.viewmodel.SavedStateHandle
import com.taetae98.diary.library.viewmodel.ViewModel
import com.taetae98.diary.navigation.core.tag.TagDetailEntry
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.core.annotation.Factory

@Factory
internal class TagDetailToolbarViewModel(
    savedStateHandle: SavedStateHandle,
    private val deleteTagUseCase: DeleteTagUseCase,
) : ViewModel() {
    private val _message = MutableStateFlow<TagDetailToolbarMessage?>(null)
    val message = _message.asStateFlow()

    private val tagId = savedStateHandle.getStateFlow(
        key = TagDetailEntry.ID,
        initialValue = "",
    )

    val uiState = TagDetailToolbarUiState(onDelete = ::delete)

    private fun delete() {
        viewModelScope.launch {
            deleteTagUseCase(TagId(tagId.value)).onSuccess {
                _message.emit(TagDetailToolbarMessage.Delete(::messageShown))
            }
        }
    }

    private fun messageShown() {
        viewModelScope.launch {
            _message.emit(null)
        }
    }
}
