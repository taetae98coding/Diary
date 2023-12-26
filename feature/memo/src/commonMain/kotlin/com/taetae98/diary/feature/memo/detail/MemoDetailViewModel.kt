package com.taetae98.diary.feature.memo.detail

import com.taetae98.diary.domain.usecase.memo.FindMemoUseCase
import com.taetae98.diary.library.viewmodel.SavedStateHandle
import com.taetae98.diary.library.viewmodel.ViewModel
import com.taetae98.diary.navigation.core.memo.MemoDetailEntry
import com.taetae98.diary.ui.compose.entity.EntityDetailUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.koin.core.annotation.Factory

@Factory
internal class MemoDetailViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val findMemoUseCase: FindMemoUseCase,
) : ViewModel() {
    private val id = savedStateHandle.getStateFlow<String?>(
        key = MemoDetailEntry.ID,
        initialValue = null
    ).onEach {
        it?.let(::onIdChange)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = null,
    )

    private val title = savedStateHandle.getStateFlow(
        key = TITLE,
        initialValue = ""
    )

    val uiState = MutableStateFlow(
        MemoDetailUiState.Detail(
            message = null,
            onMessageShown = {},
        )
    )

    val detailUiState = title.mapLatest {
        EntityDetailUiState(
            title = it,
            setTitle = ::setTitle,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = EntityDetailUiState(
            title = title.value,
            setTitle = ::setTitle,
        )
    )

    private fun onIdChange(id: String) {
        viewModelScope.launch {
            val memo = findMemoUseCase(id).getOrNull()

            setTitle(memo?.title.orEmpty())
        }
    }

    private fun setTitle(title: String) {
        savedStateHandle[TITLE] = title
    }

    companion object {
        private const val TITLE = "title"
    }
}