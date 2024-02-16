package com.taetae98.diary.feature.finished.memo

import app.cash.paging.PagingData
import com.taetae98.diary.library.viewmodel.ViewModel
import com.taetae98.diary.ui.memo.compose.MemoUiState
import kotlinx.coroutines.flow.flowOf
import org.koin.core.annotation.Factory

@Factory
internal class FinishedMemoViewModel : ViewModel() {
    val memoPaging = flowOf(PagingData.from(listOf(MemoUiState("id", "title"))))
}