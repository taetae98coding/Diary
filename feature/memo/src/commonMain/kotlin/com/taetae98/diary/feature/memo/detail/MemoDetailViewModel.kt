package com.taetae98.diary.feature.memo.detail

import com.taetae98.diary.library.viewmodel.ViewModel
import com.taetae98.diary.ui.compose.entity.EntityDetailUiState
import kotlinx.coroutines.flow.MutableStateFlow
import org.koin.core.annotation.Factory

@Factory
internal class MemoDetailViewModel : ViewModel() {
    val uiState = MutableStateFlow(
        MemoDetailUiState.Detail(
            message = null,
            onMessageShown = {},
        )
    )

    val detailUiState = MutableStateFlow(
        EntityDetailUiState(
            "",
            {},
        )
    )
}