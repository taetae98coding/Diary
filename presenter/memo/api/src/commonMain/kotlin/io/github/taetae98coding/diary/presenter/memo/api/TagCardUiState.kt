package io.github.taetae98coding.diary.presenter.memo.api

public sealed class TagCardUiState {
    public data object Loading : TagCardUiState()

    public data class State(val tagList: List<MemoTagUiState>) : TagCardUiState()
}
