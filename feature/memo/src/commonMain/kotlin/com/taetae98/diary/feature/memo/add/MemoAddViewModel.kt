package com.taetae98.diary.feature.memo.add

import com.taetae98.diary.domain.usecase.memo.UpsertMemoUseCase
import com.taetae98.diary.feature.memo.detail.MemoDetailUiState
import com.taetae98.diary.library.viewmodel.ViewModel
import com.taetae98.diary.ui.compose.entity.EntityDetailUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.koin.core.annotation.Factory

@Factory
internal class MemoAddViewModel(
    private val upsertMemoUseCase: UpsertMemoUseCase,
) : ViewModel() {
    private val _title = MutableStateFlow("")

    val uiState = MemoDetailUiState.Add(
        onAdd = ::add
    )

    val detailUiState = _title.map { title ->
        EntityDetailUiState(
            title = title,
            setTitle = ::setTitle,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = EntityDetailUiState(
            title = _title.value,
            setTitle = ::setTitle,
        )
    )

    private fun setTitle(title: String) {
        viewModelScope.launch {
            _title.emit(title)
        }
    }

    private fun add() {

    }
}