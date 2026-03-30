package io.github.taetae98coding.diary.presenter.memo.compose.list

public sealed class MemoListNavigation {
    public data object None : MemoListNavigation()
    public data class NavigateUp(val onNavigateUp: () -> Unit) : MemoListNavigation()
}
