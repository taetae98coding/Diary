package com.taetae98.diary.feature.tag.memo

import com.taetae98.diary.domain.entity.tag.TagId
import com.taetae98.diary.domain.usecase.tag.FindTagByIdUseCase
import com.taetae98.diary.library.viewmodel.SavedStateHandle
import com.taetae98.diary.library.viewmodel.ViewModel
import com.taetae98.diary.navigation.core.tag.TagMemoEntry
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.koin.core.annotation.Factory

@OptIn(ExperimentalCoroutinesApi::class)
@Factory
internal class TagMemoViewModel(
    savedStateHandle: SavedStateHandle,
    private val findTagByIdUseCase: FindTagByIdUseCase,
) : ViewModel() {
    private val tagId = savedStateHandle.getStateFlow<String>(
        key = TagMemoEntry.TAG_ID,
        initialValue = "",
    )

    private val tag = tagId.flatMapLatest { findTagByIdUseCase(TagId(it)) }
        .mapLatest { it.getOrNull() }

    private val _message = MutableStateFlow<TagMemoMessage?>(null)
    val message = _message.asStateFlow()

    val title = tag.mapLatest { it?.title.orEmpty() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = "",
        )

    init {
        viewModelScope.launch {
            launch { collectTagExist() }
        }
    }

    private suspend fun collectTagExist() {
        tag.collectLatest {
            if (it == null) {
                _message.emit(TagMemoMessage.NotFound(::messageShown))
            }
        }
    }

    private fun messageShown() {
        viewModelScope.launch {
            _message.emit(null)
        }
    }
}