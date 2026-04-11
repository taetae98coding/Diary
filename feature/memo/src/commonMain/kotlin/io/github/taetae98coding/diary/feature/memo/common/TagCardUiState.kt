package io.github.taetae98coding.diary.feature.memo.common

internal sealed class TagCardUiState {
    data object Loading : TagCardUiState()

    data class State(val tagList: List<MemoTagUiState>) : TagCardUiState()
}
